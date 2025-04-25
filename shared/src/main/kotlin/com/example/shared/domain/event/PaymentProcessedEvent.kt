package com.example.shared.domain.event

data class PaymentProcessedEvent(
    val paymentId: Int,
    val orderId: Int,
    val amount: Double
) : BaseEvent