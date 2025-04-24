package com.example.payment.data.repository

import com.example.payment.domain.model.Payment
import java.time.LocalDateTime

// Repository interface
interface PaymentRepository {
    suspend fun create(orderId: Int, paymentDate: LocalDateTime, amount: Double, paymentStatus: String): Payment
    suspend fun findById(id: Int): Payment?
    suspend fun update(id: Int, orderId: Int, paymentDate: LocalDateTime, amount: Double, paymentStatus: String): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun findAll(): List<Payment>
}
