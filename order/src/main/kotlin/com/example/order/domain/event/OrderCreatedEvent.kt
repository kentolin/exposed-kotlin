package com.example.order.domain.event

import com.example.common.domain.event.BaseEvent

data class OrderCreatedEvent(
    val orderId: Int,
    val userId: Int,
    val totalAmount: Double,
    val orderDate: String
) : BaseEvent