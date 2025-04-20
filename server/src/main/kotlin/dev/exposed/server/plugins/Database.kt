package dev.exposed.server.plugins

import dev.exposed.server.database.DatabaseFactory
import dev.exposed.server.database.DatabaseFactoryImpl
import dev.exposed.server.database.SchemaConfig
import dev.exposed.server.db.UserController
import dev.exposed.server.db.UserRepositoryImpl
import dev.exposed.server.db.UserServiceImpl
import io.ktor.server.application.*

fun Application.configureDatabases(){
    val db = DatabaseFactoryImpl()
    SchemaConfig.initialize(db)
}