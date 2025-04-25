package com.example.shared.domain.event

data class OrderCreatedEvent(
    val orderId: Int,
    val userId: Int,
    val totalAmount: Double,
    val orderDate: String
) : BaseEvent