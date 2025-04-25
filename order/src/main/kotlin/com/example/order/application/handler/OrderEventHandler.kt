package com.example.order.application.handler

import com.example.shared.domain.event.PaymentProcessedEvent

class OrderEventHandler {
    fun handle(event: PaymentProcessedEvent) {
        // Update order status (e.g., mark as paid)
        println("Order ${event.orderId} updated after payment ${event.paymentId}")
    }
}