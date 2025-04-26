package com.example.payment.plugins

import com.example.database.DatabaseFactory
import com.example.database.DatabaseFactoryImpl
import com.example.di.core.DIContainer
import com.example.di.dsl.module
import com.example.mb.application.service.EventPublisher
import com.example.mb.application.service.MessageBrokerEventPublisher
import com.example.payment.api.controller.PaymentController
import com.example.payment.application.handler.PaymentEventHandler
import com.example.payment.application.service.PaymentService
import com.example.payment.application.service.PaymentServiceFacade
import com.example.payment.application.service.PaymentServiceImpl
import com.example.payment.data.repository.PaymentRepository
import com.example.payment.data.repository.PaymentRepositoryImpl
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
                serviceUrls = listOf("http://localhost:8081", "http://localhost:8082"),
                serviceId = "payment"
            )
        }
    }
    val paymentModule = module {
        factory<PaymentRepository> { container -> PaymentRepositoryImpl(container.get()) }
        single<PaymentService> { container -> PaymentServiceImpl(container.get()) }
        single<PaymentEventHandler> { PaymentEventHandler() }
        single<PaymentServiceFacade> { container-> PaymentServiceFacade(container.get(), container.get()) }
        single<PaymentController> { container -> PaymentController(container.get(), container.get()) }


    }
    container.loadModule(coreModule)
    container.loadModule(paymentModule)
    application.diContainer = container
}

fun Application.configureDependencyInjection() {
    install(DependencyInjectionPlugin)
}