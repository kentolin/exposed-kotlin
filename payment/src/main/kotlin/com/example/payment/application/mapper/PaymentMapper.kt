package com.example.payment.application.mapper

import com.example.payment.domain.dto.PaymentDTO
import com.example.payment.domain.model.Payment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Mapper
object PaymentMapper {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun toDTO(payment: Payment): PaymentDTO = PaymentDTO(
        payment.id,
        payment.orderId,
        payment.paymentDate.format(formatter),
        payment.amount,
        payment.paymentStatus
    )

    fun toPayment(dto: PaymentDTO): Payment = Payment(
        dto.id,
        dto.orderId,
        LocalDateTime.parse(dto.paymentDate, formatter),
        dto.amount,
        dto.paymentStatus
    )

    fun toDTOs(payments: List<Payment>): List<PaymentDTO> = payments.map { toDTO(it) }
}