package dev.exposed.server.container

import kotlin.reflect.KClass

// Represents a dependency definition
sealed class Dependency {
    // Singleton: Same instance reused
    data class Singleton(val instance: Any) : Dependency()
    // Factory: New instance created each time
    data class Factory(val creator: () -> Any) : Dependency()
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

// DI Container to manage dependencies
class DIContainer {
    private val dependencies = mutableMapOf<KClass<*>, Dependency>()

    // Register a singleton instance
    fun <T : Any> single(instance: T) {
        dependencies[instance::class] = Dependency.Singleton(instance)
    }

    // Register a factory function
    fun <T : Any> factory(creator: () -> T) {
        dependencies[creator()::class] = Dependency.Factory(creator)
    }

    // Resolve a dependency by type
    inline fun <reified T : Any> get(): T = get(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(kClass: KClass<T>): T {
        val dependency = dependencies[kClass]
            ?: throw IllegalArgumentException("No dependency registered for ${kClass.simpleName}")

        return when (dependency) {
            is Dependency.Singleton -> dependency.instance as T
            is Dependency.Factory -> dependency.creator() as T
        }
    }

    // Resolve a dependency with constructor injection
    inline fun <reified T : Any> resolve(): T {
        val kClass = T::class
        val constructor = kClass.constructors.firstOrNull()
            ?: throw IllegalArgumentException("No constructor found for ${kClass.simpleName}")

        // Resolve constructor parameters recursively
        val parameters = constructor.parameters.map { param ->
            val paramClass = param.type.classifier as? KClass<*>
                ?: throw IllegalArgumentException("Cannot resolve parameter type for ${param.name}")
            get(paramClass)
        }

        return constructor.call(*parameters.toTypedArray()) as T
    }
}