package com.example.order.plugins

import com.example.database.DatabaseFactory
import com.example.database.DatabaseFactoryImpl
import com.example.di.core.DIContainer
import com.example.di.dsl.module
import com.example.order.api.controller.OrderController
import com.example.order.api.routes.orderRouting
import com.example.order.application.service.OrderService
import com.example.order.application.service.OrderServiceImpl
import com.example.order.data.repository.OrderRepository
import com.example.order.data.repository.OrderRepositoryImpl

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){

    // Create the DI container
    val container = DIContainer()

    // Define a module
    val coreModule = module {
        single<DatabaseFactory> { DatabaseFactoryImpl() }
    }



    val orderModule = module {
        factory<OrderRepository> { container -> OrderRepositoryImpl(container.get()) }
        single<OrderService> { container -> OrderServiceImpl(container.get()) }
        single<OrderController> { container -> OrderController(container.get()) }
    }

    // Load the module
    container.loadModule(coreModule)
    container.loadModule(orderModule)



    // Resolve Controller
    val orderController = container.resolve<OrderController>()

    routing {
        orderRouting(orderController)
    }
}