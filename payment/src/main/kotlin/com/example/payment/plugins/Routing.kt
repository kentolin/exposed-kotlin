package com.example.payment.plugins

import com.example.database.DatabaseFactory
import com.example.database.DatabaseFactoryImpl
import com.example.di.core.DIContainer
import com.example.di.dsl.module
import com.example.payment.api.controller.PaymentController
import com.example.payment.api.routes.paymentRouting
import com.example.payment.application.service.PaymentService
import com.example.payment.application.service.PaymentServiceImpl
import com.example.payment.data.repository.PaymentRepository
import com.example.payment.data.repository.PaymentRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){

    // Create the DI container
    val container = DIContainer()

    // Define a module
    val coreModule = module {
        single<DatabaseFactory> { DatabaseFactoryImpl() }
    }

    val paymentModule = module {
        factory<PaymentRepository> { container -> PaymentRepositoryImpl(container.get()) }
        single<PaymentService> { container -> PaymentServiceImpl(container.get()) }
        single<PaymentController> { container -> PaymentController(container.get()) }
    }


    // Load the module
    container.loadModule(coreModule)
    container.loadModule(paymentModule)




    // Resolve Controller
    val paymentController = container.resolve<PaymentController>()

    routing {
        paymentRouting(paymentController)
    }
}