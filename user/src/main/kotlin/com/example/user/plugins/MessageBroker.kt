package com.example.user.plugins

import com.example.mb.application.service.EventPublisher
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.user.application.handler.UserEventHandler
import io.ktor.server.application.*

// Custom Message Broker Plugin
val MessageBrokerPlugin = createApplicationPlugin("MessageBrokerPlugin") {

    val eventPublisher = application.diContainer.get<EventPublisher>()
    val userEventHandler = application.diContainer.get<UserEventHandler>()

    // Subscribe to OrderCreatedEvent
    eventPublisher.subscribe("OrderCreatedEvent") { event ->
        userEventHandler.handle(event as OrderCreatedEvent)
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