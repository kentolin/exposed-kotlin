package com.example.user.domain.event

import com.example.common.domain.event.BaseEvent

data class UserCreatedEvent(
    val userId: Int,
    val name: String,
    val email: String
) : BaseEvent