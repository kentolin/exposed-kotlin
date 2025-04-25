package com.example.mb.application.service

import com.example.mb.domain.event.BaseEvent
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.mb.domain.event.UserCreatedEvent
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class MessageBrokerEventPublisher : EventPublisher {
    private val topics = ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>()
    private val subscribers = ConcurrentHashMap<String, MutableList<(Any) -> Unit>>()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(BaseEvent::class) {
                subclass(UserCreatedEvent::class)
                subclass(OrderCreatedEvent::class)
            }
        }
    }

    init {
        scope.launch {
            while (isActive) {
                topics.forEach { (topic, queue) ->
                    while (queue.isNotEmpty()) {
                        val eventJson = queue.poll() ?: continue
                        val handlers = subscribers[topic] ?: emptyList()
                        handlers.forEach { handler ->
                            launch {
                                try {
                                    val event = when (topic) {
                                        "events.UserCreatedEvent" -> json.decodeFromString<UserCreatedEvent>(eventJson)
                                        "events.OrderCreatedEvent" -> json.decodeFromString<OrderCreatedEvent>(eventJson)
                                        else -> return@launch
                                    }
                                    handler(event)
                                } catch (e: Exception) {
                                    println("Error processing event for topic $topic: $e")
                                }
                            }
                        }
                    }
                }
                delay(10)
            }
        }
    }

    override fun publish(event: Any) {
        val eventType = event::class.simpleName ?: return
        val topic = "events.$eventType"
        val json = when (event) {
            is UserCreatedEvent -> json.encodeToString(event)
            is OrderCreatedEvent -> json.encodeToString(event)
            else -> return // Ignore unknown event types
        }
        topics.computeIfAbsent(topic) { ConcurrentLinkedQueue() }.add(json)
    }

    override fun subscribe(eventType: String, handler: (Any) -> Unit) {
        val topic = "events.$eventType"
        subscribers.computeIfAbsent(topic) { mutableListOf() }.add(handler)
        println("Subscribed to topic: $topic")
    }

    fun shutdown() {
        scope.cancel()
    }
}
