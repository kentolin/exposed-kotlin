package com.example.order.application.service

import kotlinx.coroutines.*

// Simple in-memory implementation (replace with Kafka/RabbitMQ in production)

class InMemoryEventPublisher : EventPublisher {
    private val subscribers = mutableMapOf<String, MutableList<(Any) -> Unit>>()

    override fun publish(event: Any) {
        val eventType = event::class.simpleName ?: return
        subscribers[eventType]?.forEach { handler ->
            CoroutineScope(Dispatchers.Default).launch {
                handler(event)
            }
        }
    }

    fun subscribe(eventType: String, handler: (Any) -> Unit) {
        subscribers.computeIfAbsent(eventType) { mutableListOf() }.add(handler)
    }
}