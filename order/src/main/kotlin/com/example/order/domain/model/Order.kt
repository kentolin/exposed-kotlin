package com.example.order.domain.model

import java.time.LocalDateTime

// Domain model
data class Order(val id: Int, val userId: Int, val orderDate: LocalDateTime, val totalAmount: Double)
