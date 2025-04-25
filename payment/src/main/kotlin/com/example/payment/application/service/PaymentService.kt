package com.example.payment.application.service

import com.example.mb.domain.dto.PaymentDTO

// Service interface
interface PaymentService {
    suspend fun create(dto: PaymentDTO): PaymentDTO
    suspend fun getById(id: Int): PaymentDTO?
    suspend fun update(id: Int, dto: PaymentDTO): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getAll(): List<PaymentDTO>
}