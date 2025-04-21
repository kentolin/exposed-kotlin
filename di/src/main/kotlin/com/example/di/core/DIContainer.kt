package com.example.di.core

import com.example.di.internal.ConstructorMetadata
import com.example.di.exception.DIException
import com.example.di.dsl.Module
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

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