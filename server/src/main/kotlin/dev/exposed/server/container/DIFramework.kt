package dev.exposed.server.container

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import java.util.concurrent.ConcurrentHashMap

// Enum to define scope types
enum class ScopeType { SINGLETON, FACTORY, REQUEST }

// Custom exception hierarchy for DI errors
sealed class DIException(message: String) : RuntimeException(message) {
    data class DependencyNotFound(
        val kClass: KClass<*>,
        val name: String
    ) : DIException("No dependency registered for ${kClass.simpleName} with name '$name'")

    data class CircularDependency(
        val chain: List<KClass<*>>
    ) : DIException("Circular dependency detected: ${chain.joinToString(" -> ") { it.simpleName ?: "<anonymous>" }}")

    data class ConstructorNotFound(
        val kClass: KClass<*>
    ) : DIException("No primary constructor found for ${kClass.simpleName}")

    data class InvalidParameter(
        val paramName: String?,
        val kClass: KClass<*>
    ) : DIException("Cannot resolve parameter ${paramName ?: "<unknown>"} for ${kClass.simpleName}")
}

// Represents a dependency definition
sealed class Dependency(val scope: ScopeType) {
    data class Singleton(val instance: Any) : Dependency(ScopeType.SINGLETON)
    data class Factory(val creator: (DIContainer) -> Any) : Dependency(ScopeType.FACTORY)
    data class Request(val creator: (DIContainer) -> Any) : Dependency(ScopeType.REQUEST)
}

// Data class to store constructor metadata
data class ConstructorMetadata(
    val constructor: KFunction<Any>,
    val parameterClasses: List<KClass<*>>
)

// DI Module to group dependency definitions
class Module {
    val definitions = mutableListOf<(DIContainer) -> Unit>()

    inline fun <reified T : Any> single(name: String = "default", noinline creator: (DIContainer) -> T) {
        definitions.add { container ->
            container.register(T::class, name, Dependency.Singleton(creator(container)))
        }
    }

    inline fun <reified T : Any> factory(name: String = "default", noinline creator: (DIContainer) -> T) {
        definitions.add { container ->
            container.register(T::class, name, Dependency.Factory(creator))
        }
    }

    inline fun <reified T : Any> request(name: String = "default", noinline creator: (DIContainer) -> T) {
        definitions.add { container ->
            container.register(T::class, name, Dependency.Request(creator))
        }
    }
}

// DI Container with performance optimizations
class DIContainer {
    private val dependencies = ConcurrentHashMap<KClass<*>, MutableMap<String, Dependency>>()
    private val constructorCache = ConcurrentHashMap<KClass<*>, ConstructorMetadata>()
    private val resolutionStack = ThreadLocal.withInitial { mutableListOf<KClass<*>>() } // Thread-local for concurrency
    private val requestScope = ConcurrentHashMap<KClass<*>, MutableMap<String, Any>>() // Thread-safe request scope

    fun register(kClass: KClass<*>, name: String = "default", dependency: Dependency) {
        val namedDeps = dependencies.getOrPut(kClass) { ConcurrentHashMap() }
        if (namedDeps.containsKey(name)) {
            println("Warning: Overwriting dependency for ${kClass.simpleName} with name '$name'")
        }
        namedDeps[name] = dependency
    }

    // Load a module with validation
    fun loadModule(module: Module) {
        // Load into main container
        module.definitions.forEach { it(this) }
    }

    // Resolve a dependency by type and name
    inline fun <reified T : Any> get(name: String = "default"): T = get(T::class, name)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(kClass: KClass<T>, name: String = "default"): T {
        val stack = resolutionStack.get()
        if (kClass in stack) {
            throw DIException.CircularDependency(stack + kClass)
        }
        stack.add(kClass)
        try {
            val namedDeps = dependencies[kClass]
                ?: throw DIException.DependencyNotFound(kClass, name)
            val dependency = namedDeps[name]
                ?: throw DIException.DependencyNotFound(kClass, name)
            return when (dependency) {
                is Dependency.Singleton -> dependency.instance as T
                is Dependency.Factory -> dependency.creator(this) as T
                is Dependency.Request -> {
                    val scopeInstances = requestScope.getOrPut(kClass) { ConcurrentHashMap() }
                    scopeInstances.getOrPut(name) { dependency.creator(this) } as T
                }
            }
        } finally {
            stack.removeLast()
        }
    }

    // Resolve with constructor injection
    inline fun <reified T : Any> resolve(): T = resolve(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(kClass: KClass<T>): T {
        val stack = resolutionStack.get()
        if (kClass in stack) {
            throw DIException.CircularDependency(stack + kClass)
        }
        stack.add(kClass)
        try {
            val metadata = constructorCache.getOrPut(kClass) {
                val constructor = kClass.primaryConstructor
                    ?: throw DIException.ConstructorNotFound(kClass)
                val parameterClasses = constructor.parameters.map { param ->
                    (param.type.classifier as? KClass<*>)
                        ?: throw DIException.InvalidParameter(param.name, kClass)
                }
                ConstructorMetadata(constructor as KFunction<Any>, parameterClasses)
            }
            val parameters = metadata.parameterClasses.map { paramClass ->
                get(paramClass) // Uses default name; extend for named constructor params
            }
            return metadata.constructor.call(*parameters.toTypedArray()) as T
        } finally {
            stack.removeLast()
        }
    }

    // Clear request-scoped dependencies
    fun clearRequestScope() {
        requestScope.clear()
    }
}

// DSL for creating modules
fun module(block: Module.() -> Unit): Module {
    return Module().apply(block)
}