package com.example.shared.application.service

// shared/application/service/InMemoryEventPublisher.kt

import kotlinx.coroutines.*

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

    override fun subscribe(eventType: String, handler: (Any) -> Unit) {
        subscribers.computeIfAbsent(eventType) { mutableListOf() }.add(handler)
    }
}
