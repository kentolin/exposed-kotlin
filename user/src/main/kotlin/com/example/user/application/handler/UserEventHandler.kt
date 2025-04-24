package com.example.user.application.handler

import com.example.order.domain.event.OrderCreatedEvent

class UserEventHandler {
    fun handle(event: OrderCreatedEvent) {
        // Example: Log or update user data
        println("User notified of order ${event.orderId}")
    }
}