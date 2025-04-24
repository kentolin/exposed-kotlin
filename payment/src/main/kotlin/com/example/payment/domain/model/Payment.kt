package com.example.payment.domain.model

import java.time.LocalDateTime

// Domain model
data class Payment(
    val id: Int,
    val orderId: Int,
    val paymentDate: LocalDateTime,
    val amount: Double,
    val paymentStatus: String
)