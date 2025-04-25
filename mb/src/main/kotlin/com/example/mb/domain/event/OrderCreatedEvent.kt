package com.example.mb.domain.event

import kotlinx.serialization.Serializable

@Serializable
data class OrderCreatedEvent(
    val orderId: Int,
    val userId: Int,
    val totalAmount: Double,
    val orderDate: String
) : BaseEvent