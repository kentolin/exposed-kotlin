package dev.exposed.server.plugins

import dev.exposed.server.container.DIContainer
import dev.exposed.server.container.module
import dev.exposed.server.database.DatabaseFactory
import dev.exposed.server.database.DatabaseFactoryImpl
import dev.exposed.server.db.*
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
    val controller = container.resolve<UserController>()

    routing {
        userRouting(controller)
    }
}