package com.example.order.application.handler

import com.example.mb.domain.event.PaymentProcessedEvent
import com.example.mb.domain.event.UserCreatedEvent

class OrderEventHandler {
    fun handle(event: PaymentProcessedEvent) {
        // Update order status (e.g., mark as paid)
        println("Order ${event.orderId} updated after payment ${event.paymentId}")
    }
    fun handle(event: UserCreatedEvent) {
        println("Order service received UserCreatedEvent: userId=${event.userId}, name=${event.name}")
    }
}