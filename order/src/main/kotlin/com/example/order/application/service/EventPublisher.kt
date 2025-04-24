package com.example.order.application.service

interface EventPublisher {
    fun publish(event: Any)
}