package com.example.user.plugins

import com.example.database.DatabaseFactory
import com.example.database.DatabaseFactoryImpl
import com.example.di.core.DIContainer
import com.example.di.dsl.module
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

    // Load the module
    container.loadModule(coreModule)
    container.loadModule(userModule)

    // Resolve Controller
    val userController = container.resolve<UserController>()

    routing {
        userRouting(userController)
    }
}