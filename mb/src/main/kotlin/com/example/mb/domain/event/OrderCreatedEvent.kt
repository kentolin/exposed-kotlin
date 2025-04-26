package com.example.mb.domain.event

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class OrderCreatedEvent(
    val orderId: Int,
    val userId: Int,
    val totalAmount: Double,
    val orderDate: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val origin: String
) : BaseEvent