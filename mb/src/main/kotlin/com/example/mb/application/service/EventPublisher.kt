package com.example.mb.application.service

// shared/application/service/EventPublisher.kt

interface EventPublisher {
    fun publish(event: Any)
    fun subscribe(eventType: String, handler: (Any) -> Unit)
}