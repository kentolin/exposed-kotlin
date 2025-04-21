package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

// Data class to store constructor metadata
data class ConstructorMetadata(
    val constructor: KFunction<Any>,
    val parameterClasses: List<KClass<*>>
)