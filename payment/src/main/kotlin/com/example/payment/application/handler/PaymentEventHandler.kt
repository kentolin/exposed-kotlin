package com.example.payment.application.handler

import com.example.mb.domain.event.OrderCreatedEvent
import com.example.mb.domain.event.PaymentProcessedEvent

class PaymentEventHandler {
    fun handle(event: OrderCreatedEvent) {
        println("User ${event.userId} notified of order ${event.orderId}")
    }
    fun handle(event: PaymentProcessedEvent) {
        println("User notified of payment ${event.paymentId} for order ${event.orderId}")
    }
}