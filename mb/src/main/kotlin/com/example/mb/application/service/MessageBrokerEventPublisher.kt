package com.example.mb.application.service

import com.example.mb.domain.event.BaseEvent
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.mb.domain.event.PaymentProcessedEvent
import com.example.mb.domain.event.UserCreatedEvent
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class MessageBrokerEventPublisher(
    private val httpClient: HttpClient? = null,
    private val serviceUrls: List<String> = emptyList(),
    private val serviceId: String
) : EventPublisher {
    private val topics = ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>()
    private val subscribers = ConcurrentHashMap<String, MutableList<(Any) -> Unit>>()
    private val processedEventIds = ConcurrentHashMap.newKeySet<String>()
    private val failedUrls = ConcurrentHashMap<String, Long>()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val httpScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(BaseEvent::class) {
                subclass(UserCreatedEvent::class)
                subclass(OrderCreatedEvent::class)
                subclass(PaymentProcessedEvent::class)
            }
        }
    }

    init {
        scope.launch {
            while (isActive) {
                var hasEvents = false
                topics.forEach { (topic, queue) ->
                    if (queue.isNotEmpty()) {
                        hasEvents = true
                        println("Broker: Processing topic $topic, queue size: ${queue.size}")
                        while (queue.isNotEmpty()) {
                            val eventJson = queue.poll() ?: continue
                            println("Broker: Processing event JSON: $eventJson")
                            val handlers = subscribers[topic] ?: emptyList()
                            println("Broker: Found ${handlers.size} handlers for topic $topic")
                            handlers.forEach { handler ->
                                launch {
                                    try {
                                        val event = when (topic) {
                                            "events.UserCreatedEvent" -> json.decodeFromString<UserCreatedEvent>(eventJson)
                                            "events.OrderCreatedEvent" -> json.decodeFromString<OrderCreatedEvent>(eventJson)
                                            "events.PaymentProcessedEvent" -> json.decodeFromString<PaymentProcessedEvent>(eventJson)
                                            else -> return@launch
                                        }
                                        if (processedEventIds.contains(event.eventId)) {
                                            println("Broker: Skipping already processed event ${event.eventId} (Origin: ${event.origin})")
                                            return@launch
                                        }
                                        if (event.origin == serviceId) {
                                            println("Broker: Skipping event from self ${event.eventId} (Origin: ${event.origin})")
                                            return@launch
                                        }
                                        if (processedEventIds.size > 10_000) {
                                            println("Broker: Clearing processed event IDs to prevent memory growth")
                                            processedEventIds.clear()
                                        }
                                        processedEventIds.add(event.eventId)
                                        println("Broker: Deserialized event: $event")
                                        handler(event)
                                    } catch (e: Exception) {
                                        println("Broker: Error processing event for topic $topic: $e")
                                    }
                                }
                            }
                        }
                    }
                }
                delay(if (hasEvents) 10 else 100)
            }
        }
    }

    override fun publish(event: Any) {
        val eventType = event::class.simpleName ?: return
        val topic = "events.$eventType"
        val baseEvent = event as? BaseEvent ?: return
        synchronized(processedEventIds) {
            if (processedEventIds.contains(baseEvent.eventId)) {
                println("Broker: Skipping publish of already processed event ${baseEvent.eventId} (Origin: ${baseEvent.origin})")
                return
            }
            processedEventIds.add(baseEvent.eventId)
        }
        val json = when (event) {
            is UserCreatedEvent -> json.encodeToString(event)
            is OrderCreatedEvent -> json.encodeToString(event)
            is PaymentProcessedEvent -> json.encodeToString(event)
            else -> return
        }
        println("Broker: Publishing event to topic $topic: $json")
        val queue = topics.computeIfAbsent(topic) { ConcurrentLinkedQueue() }
        if (queue.size > 1000) {
            println("Broker: Warning: Queue for topic $topic is full (size: ${queue.size}). Dropping event.")
            return
        }
        queue.add(json)

        httpClient?.let { client ->
            httpScope.launch {
                serviceUrls.forEach { url ->
                    if (failedUrls.containsKey(url) && System.currentTimeMillis() - failedUrls[url]!! < 30_000) {
                        println("Broker: Skipping $url due to recent failure")
                        return@forEach
                    }
                    try {
                        withTimeout(5000) {
                            client.post("$url/events") {
                                header("Event-Type", eventType)
                                header("Event-Id", baseEvent.eventId)
                                header("Origin", baseEvent.origin)
                                contentType(ContentType.Application.Json)
                                setBody(json)
                            }
                            println("Broker: Sent event $eventType to $url")
                            failedUrls.remove(url)
                        }
                    } catch (e: Exception) {
                        println("Broker: Failed to send event $eventType to $url: $e")
                        failedUrls[url] = System.currentTimeMillis()
                    }
                }
            }
        }
    }

    override fun subscribe(eventType: String, handler: (Any) -> Unit) {
        val topic = "events.$eventType"
        subscribers.computeIfAbsent(topic) { mutableListOf() }.add(handler)
        println("Broker: Subscribed to topic $topic, total handlers: ${subscribers[topic]?.size}")
    }

    fun shutdown() {
        scope.cancel()
        httpScope.cancel()
    }
}