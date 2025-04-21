package com.example.server.plugins

import com.example.database.DatabaseFactory
import com.example.database.DatabaseFactoryImpl
import com.example.di.DIContainer
import com.example.di.module
import com.example.order.api.controller.OrderController
import com.example.order.api.routes.orderRouting
import com.example.order.application.service.OrderService
import com.example.order.application.service.OrderServiceImpl
import com.example.order.data.repository.OrderRepository
import com.example.order.data.repository.OrderRepositoryImpl
import com.example.order.domain.model.Order
import com.example.user.api.controller.UserController
import com.example.user.api.routes.userRouting
import com.example.user.application.service.UserService
import com.example.user.application.service.UserServiceImpl
import com.example.user.data.repository.UserRepository
import com.example.user.data.repository.UserRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){

    // Create the DI container
    val container = DIContainer()

    // Define a module
    val coreModule = module {
        single<DatabaseFactory> { DatabaseFactoryImpl() }
    }

    val userModule = module {
        factory<UserRepository> { container -> UserRepositoryImpl(container.get()) }
        single<UserService> { container -> UserServiceImpl(container.get()) }
        single<UserController> { container -> UserController(container.get()) }
    }

    val orderModule = module {
        factory<OrderRepository> { container -> OrderRepositoryImpl(container.get()) }
        single<OrderService> { container -> OrderServiceImpl(container.get()) }
        single<OrderController> { container -> OrderController(container.get()) }
    }

    // Load the module
    container.loadModule(coreModule)
    container.loadModule(userModule)
    container.loadModule(orderModule)


    // Resolve Controller
    val userController = container.resolve<UserController>()
    val orderController = container.resolve<OrderController>()

    routing {
        userRouting(userController)
        orderRouting(orderController)
    }
}