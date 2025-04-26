package com.example.user.domain.event

import com.example.mb.domain.event.BaseEvent
import kotlinx.serialization.Serializable

@Serializable
data class UserSpecificEvent(val userId: Int, override val eventId: String, override val origin: String) : BaseEvent // Placeholder