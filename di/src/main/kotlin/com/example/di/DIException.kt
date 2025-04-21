package com.example.di

import kotlin.reflect.KClass


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
