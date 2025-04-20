package dev.exposed.server.plugins

import dev.exposed.server.container.DIContainer
import dev.exposed.server.database.DatabaseFactory
import dev.exposed.server.database.DatabaseFactoryImpl
import dev.exposed.server.db.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){

    val container = DIContainer()
    container.factory<DatabaseFactory> { DatabaseFactoryImpl() }
    container.factory<UserRepository> { UserRepositoryImpl(container.get(DatabaseFactory::class)) }
    container.factory<UserService> { UserServiceImpl(container.get(UserRepository::class)) }

    val controller = container.resolve<UserController>()

    routing {
        userRouting(controller)
    }
}