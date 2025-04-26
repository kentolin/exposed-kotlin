package com.example.payment

import com.example.mb.application.service.EventPublisher
import com.example.mb.application.service.MessageBrokerEventPublisher
import com.example.payment.plugins.*
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
        println("Payment: Shutting down application")
        val eventPublisher = diContainer.get<EventPublisher>()
        if (eventPublisher is MessageBrokerEventPublisher) {
            eventPublisher.shutdown()
        }
        val httpClient = diContainer.get<HttpClient>()
        runBlocking {
            httpClient.close()
        }
        println("Payment: Shutdown complete")
    }

}