package com.example.order.plugins

import com.example.database.DatabaseFactory
import com.example.database.DatabaseFactoryImpl
import com.example.di.core.DIContainer
import com.example.di.dsl.module
import com.example.mb.application.service.EventPublisher
import com.example.mb.application.service.MessageBrokerEventPublisher
import com.example.order.api.controller.OrderController
import com.example.order.application.handler.OrderEventHandler
import com.example.order.application.service.OrderService
import com.example.order.application.service.OrderServiceFacade
import com.example.order.application.service.OrderServiceImpl
import com.example.order.data.repository.OrderRepository
import com.example.order.data.repository.OrderRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.util.*

var Application.diContainer: DIContainer
    get() = this.attributes.getOrNull(AttributeKey("DIContainer"))
        ?: throw IllegalStateException("DIContainer not initialized")
    set(value) { this.attributes.put(AttributeKey("DIContainer"), value) }

// Custom DI Plugin
val DependencyInjectionPlugin = createApplicationPlugin("DependencyInjectionPlugin") {
    val container = DIContainer()
    val coreModule = module {
        single<DatabaseFactory> { DatabaseFactoryImpl() }
        single<HttpClient> {
            HttpClient(CIO) {
                install(ContentNegotiation) { json() }
            }
        }
        single<EventPublisher> {
            MessageBrokerEventPublisher(
                httpClient = container.get(),
                serviceUrls = listOf("http://localhost:8081", "http://localhost:8083"),
                serviceId = "order"
            )
        }
    }
    val orderModule = module {
        factory<OrderRepository> { container -> OrderRepositoryImpl(container.get()) }
        single<OrderService> { container -> OrderServiceImpl(container.get()) }
        single<OrderEventHandler> { OrderEventHandler() }
        single<OrderServiceFacade> { container-> OrderServiceFacade(container.get(), container.get()) }
        single<OrderController> { container -> OrderController(container.get(), container.get()) }


    }
    container.loadModule(coreModule)
    container.loadModule(orderModule)
    application.diContainer = container
}

fun Application.configureDependencyInjection() {
    install(DependencyInjectionPlugin)
}