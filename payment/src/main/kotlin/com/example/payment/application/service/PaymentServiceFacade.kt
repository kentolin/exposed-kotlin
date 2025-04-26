package com.example.payment.application.service

import com.example.mb.application.service.EventPublisher
import com.example.mb.domain.dto.PaymentDTO
import com.example.mb.domain.event.PaymentProcessedEvent


class PaymentServiceFacade(
    private val paymentService: PaymentService,
    private val eventPublisher: EventPublisher
) {
    suspend fun create(dto: PaymentDTO): PaymentDTO {
        val processedPayment = paymentService.create(dto)
        val event = PaymentProcessedEvent(
            paymentId = processedPayment.id,
            orderId = processedPayment.orderId, amount = processedPayment.amount,
            origin = "payment"
        )
        eventPublisher.publish(event)
        return processedPayment
    }
}