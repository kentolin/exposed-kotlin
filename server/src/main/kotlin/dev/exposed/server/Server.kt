package dev.exposed.server

import dev.exposed.server.plugins.configureDatabases
import dev.exposed.server.plugins.configureRouting
import dev.exposed.server.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*


fun main(args: Array<String>){
    EngineMain.main(args)
}

fun Application.module(){
    configureDatabases()
    configureRouting()
    configureSerialization()
}