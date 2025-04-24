package com.example.payment.application.service

import com.example.payment.application.mapper.PaymentMapper
import com.example.payment.data.repository.PaymentRepository
import com.example.payment.domain.dto.PaymentDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Service implementation
class PaymentServiceImpl(
    private val repository: PaymentRepository
) : PaymentService {
    override suspend fun create(dto: PaymentDTO): PaymentDTO {
        val payment = repository.create(
            dto.orderId,
            LocalDateTime.parse(dto.paymentDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            dto.amount,
            dto.paymentStatus
        )
        return PaymentMapper.toDTO(payment)
    }

    override suspend fun getById(id: Int): PaymentDTO? {
        return repository.findById(id)?.let { PaymentMapper.toDTO(it) }
    }

    override suspend fun update(id: Int, dto: PaymentDTO): Boolean {
        return repository.update(
            id,
            dto.orderId,
            LocalDateTime.parse(dto.paymentDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            dto.amount,
            dto.paymentStatus
        )
    }

    override suspend fun delete(id: Int): Boolean {
        return repository.delete(id)
    }

    override suspend fun getAll(): List<PaymentDTO> {
        return PaymentMapper.toDTOs(repository.findAll())
    }
}