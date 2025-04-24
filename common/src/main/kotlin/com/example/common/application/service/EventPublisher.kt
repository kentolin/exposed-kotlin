package com.example.common.application.service

interface EventPublisher {
    fun publish(event: Any)
    fun subscribe(eventType: String, handler: (Any) -> Unit)
}