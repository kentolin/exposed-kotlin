package com.example.mb.domain.event

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PaymentProcessedEvent(
    val paymentId: Int,
    val orderId: Int,
    val amount: Double,
    override val eventId: String = UUID.randomUUID().toString(),
    override val origin: String
) : BaseEvent