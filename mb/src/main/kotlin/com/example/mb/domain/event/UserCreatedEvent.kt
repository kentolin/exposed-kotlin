package com.example.mb.domain.event

import kotlinx.serialization.Serializable

@Serializable
data class UserCreatedEvent(
    val userId: Int,
    val name: String
) : BaseEvent