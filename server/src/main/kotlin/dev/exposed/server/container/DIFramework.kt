package dev.exposed.server.container

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

// Represents a dependency definition
sealed class Dependency {
    data class Singleton(val instance: Any) : Dependency()
    data class Factory(val creator: (DIContainer) -> Any) : Dependency()
}

// DI Module to group dependency definitions
class Module {
    val definitions = mutableListOf<(DIContainer) -> Unit>()

    inline fun <reified T : Any> single(noinline creator: (DIContainer) -> T) {
        definitions.add { container ->
            container.register(T::class, Dependency.Singleton(creator(container)))
        }
    }

    inline fun <reified T : Any> factory(noinline creator: (DIContainer) -> T) {
        definitions.add { container ->
            container.register(T::class, Dependency.Factory(creator))
        }
    }
}

// DI Container
class DIContainer {
    private val dependencies = mutableMapOf<KClass<*>, Dependency>()

    fun register(kClass: KClass<*>, dependency: Dependency) {
        dependencies[kClass] = dependency
    }

    // Load a module
    fun loadModule(module: Module) {
        module.definitions.forEach { it(this) }
    }

    // Resolve a dependency by type
    inline fun <reified T : Any> get(): T = get(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(kClass: KClass<T>): T {
        val dependency = dependencies[kClass]
            ?: throw IllegalArgumentException("No dependency registered for ${kClass.simpleName}")
        return when (dependency) {
            is Dependency.Singleton -> dependency.instance as T
            is Dependency.Factory -> dependency.creator(this) as T
        }
    }

    // Resolve with constructor injection
    inline fun <reified T : Any> resolve(): T {
        val kClass = T::class
        val constructor = kClass.primaryConstructor
            ?: throw IllegalArgumentException("No primary constructor for ${kClass.simpleName}")

        val parameters = constructor.parameters.map { param ->
            val paramClass = param.type.classifier as? KClass<*>
                ?: throw IllegalArgumentException("Cannot resolve parameter type ${param.name}")
            get(paramClass)
        }

        return constructor.call(*parameters.toTypedArray()) as T
    }
}

// DSL for creating modules
fun module(block: Module.() -> Unit): Module {
    return Module().apply(block)
}