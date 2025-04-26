package com.example.order


import com.example.order.plugins.*
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