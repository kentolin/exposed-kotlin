package com.example.mb.domain.event

import kotlinx.serialization.Serializable

@Serializable
data class PaymentProcessedEvent(
    val paymentId: Int,
    val orderId: Int,
    val amount: Double
) : BaseEvent