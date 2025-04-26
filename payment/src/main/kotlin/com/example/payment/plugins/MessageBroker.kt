package com.example.payment.plugins

import com.example.mb.application.service.EventPublisher
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.mb.domain.event.PaymentProcessedEvent
import com.example.payment.application.handler.PaymentEventHandler
import io.ktor.server.application.*

// Custom Message Broker Plugin
val MessageBrokerPlugin = createApplicationPlugin("MessageBrokerPlugin") {

    val eventPublisher = application.diContainer.get<EventPublisher>()
    val paymentEventHandler = application.diContainer.get<PaymentEventHandler>()

    // Subscribe to OrderCreatedEvent
    eventPublisher.subscribe("OrderCreatedEvent") { event ->
        paymentEventHandler.handle(event as OrderCreatedEvent)
    }

    // Temporary: Subscribe to UserCreatedEvent to verify publishing
    eventPublisher.subscribe("UserCreatedEvent") { event ->
        println("Received UserCreatedEvent: $event")
    }
}

fun Application.configureMessageBroker() {
    // Ensure DependencyInjectionPlugin is installed
    pluginOrNull(DependencyInjectionPlugin) ?: install(DependencyInjectionPlugin)
    install(MessageBrokerPlugin)
}