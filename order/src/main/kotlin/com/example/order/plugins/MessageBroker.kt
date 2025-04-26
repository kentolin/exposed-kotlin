package com.example.order.plugins

import com.example.mb.application.service.EventPublisher
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.mb.domain.event.PaymentProcessedEvent
import com.example.mb.domain.event.UserCreatedEvent
import com.example.order.application.handler.OrderEventHandler
import io.ktor.server.application.*

// Custom Message Broker Plugin
val MessageBrokerPlugin = createApplicationPlugin("MessageBrokerPlugin") {

    val eventPublisher = application.diContainer.get<EventPublisher>()
    val orderEventHandler = application.diContainer.get<OrderEventHandler>()

    eventPublisher.subscribe("PaymentProcessedEvent") { event ->
        orderEventHandler.handle(event as PaymentProcessedEvent)
    }
    eventPublisher.subscribe("UserCreatedEvent") { event ->
        orderEventHandler.handle(event as UserCreatedEvent)
    }
}

fun Application.configureMessageBroker() {
    // Ensure DependencyInjectionPlugin is installed
    pluginOrNull(DependencyInjectionPlugin) ?: install(DependencyInjectionPlugin)
    install(MessageBrokerPlugin)
}