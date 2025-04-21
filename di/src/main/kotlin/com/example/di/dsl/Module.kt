package com.example.di.dsl

import com.example.di.core.DIContainer
import com.example.di.core.Dependency

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

// DSL for creating modules
fun module(block: Module.() -> Unit): Module {
    return Module().apply(block)
}