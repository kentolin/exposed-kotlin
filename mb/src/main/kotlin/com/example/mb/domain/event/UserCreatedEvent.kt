package com.example.mb.domain.event

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserCreatedEvent(
    val userId: Int,
    val name: String,
    override val eventId: String,
    override val origin: String
) : BaseEvent