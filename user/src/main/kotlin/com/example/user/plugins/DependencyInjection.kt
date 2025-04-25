package com.example.user.plugins

import com.example.database.DatabaseFactory
import com.example.database.DatabaseFactoryImpl
import com.example.di.core.DIContainer
import com.example.di.dsl.module
import com.example.mb.application.service.EventPublisher
import com.example.mb.application.service.MessageBrokerEventPublisher
import com.example.user.api.controller.UserController
import com.example.user.application.handler.UserEventHandler
import com.example.user.application.service.UserService
import com.example.user.application.service.UserServiceFacade
import com.example.user.application.service.UserServiceImpl
import com.example.user.data.repository.UserRepository
import com.example.user.data.repository.UserRepositoryImpl
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
        single<EventPublisher> { MessageBrokerEventPublisher() }
    }
    val userModule = module {
        factory<UserRepository> { container -> UserRepositoryImpl(container.get()) }
        single<UserService> { container -> UserServiceImpl(container.get()) }
        single<UserEventHandler> { UserEventHandler() }
        single<UserServiceFacade> { container-> UserServiceFacade(container.get(), container.get()) }
        single<UserController> { container -> UserController(container.get(), container.get()) }


    }
    container.loadModule(coreModule)
    container.loadModule(userModule)
    application.diContainer = container
}

fun Application.configureDependencyInjection() {
    install(DependencyInjectionPlugin)
}