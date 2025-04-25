package com.example.user

import com.example.user.plugins.configureDatabases
import com.example.user.plugins.configureDependencyInjection
import com.example.user.plugins.configureMessageBroker
import com.example.user.plugins.configureRouting
import com.example.user.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>){
    EngineMain.main(args)
}
@Suppress("unused")
fun Application.module(){
    configureSerialization()
    configureDatabases()
    configureDependencyInjection()
    configureMessageBroker()
    configureRouting()

}