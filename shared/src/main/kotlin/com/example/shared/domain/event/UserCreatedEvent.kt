package com.example.shared.domain.event

data class UserCreatedEvent(
    val userId: Int,
    val name: String,
    val email: String
) : BaseEvent