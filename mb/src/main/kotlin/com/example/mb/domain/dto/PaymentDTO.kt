package com.example.mb.domain.dto

import kotlinx.serialization.Serializable

// DTO for serialization
@Serializable
data class PaymentDTO(
    val id: Int,
    val orderId: Int,
    val paymentDate: String,
    val amount: Double,
    val paymentStatus: String
)