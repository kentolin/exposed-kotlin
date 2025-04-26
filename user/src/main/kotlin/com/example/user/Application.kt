package com.example.user

import com.example.mb.application.service.EventPublisher
import com.example.mb.application.service.MessageBrokerEventPublisher
import com.example.user.plugins.configureDatabases
import com.example.user.plugins.configureDependencyInjection
import com.example.user.plugins.configureMessageBroker
import com.example.user.plugins.configureRouting
import com.example.user.plugins.configureSerialization
import com.example.user.plugins.diContainer
import io.ktor.client.HttpClient
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking

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

    // Shutdown hook
    environment.monitor.subscribe(ApplicationStopping) {
        println("User: Shutting down application")
        val eventPublisher = diContainer.get<EventPublisher>()
        if (eventPublisher is MessageBrokerEventPublisher) {
            eventPublisher.shutdown()
        }
        val httpClient = diContainer.get<HttpClient>()
        runBlocking {
            httpClient.close()
        }
        println("User: Shutdown complete")
    }
}

