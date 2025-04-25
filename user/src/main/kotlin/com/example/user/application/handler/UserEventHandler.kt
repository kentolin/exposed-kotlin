package com.example.user.application.handler

import com.example.mb.domain.event.OrderCreatedEvent

class UserEventHandler {
    fun handle(event: OrderCreatedEvent) {
        println("User ${event.userId} notified of order ${event.orderId}")
    }
}