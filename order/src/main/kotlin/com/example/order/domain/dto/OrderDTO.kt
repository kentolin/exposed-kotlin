package com.example.order.domain.dto

import kotlinx.serialization.Serializable

// DTO for serialization
@Serializable
data class OrderDTO(val id: Int, val userId: Int, val orderDate: String, val totalAmount: Double)