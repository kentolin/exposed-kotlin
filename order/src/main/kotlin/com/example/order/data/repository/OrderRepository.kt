package com.example.order.data.repository

import com.example.order.domain.model.Order
import java.time.LocalDateTime

// Repository interface
interface OrderRepository {
    suspend fun create(userId: Int, orderDate: LocalDateTime, totalAmount: Double): Order
    suspend fun findById(id: Int): Order?
    suspend fun update(id: Int, userId: Int, orderDate: LocalDateTime, totalAmount: Double): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun findAll(): List<Order>
}