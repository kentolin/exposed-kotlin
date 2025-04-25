package com.example.payment

import com.example.payment.plugins.configureDatabases
import com.example.payment.plugins.configureRouting
import com.example.payment.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>){
    EngineMain.main(args)
}
@Suppress("unused")
fun Application.module(){
    configureSerialization()
    configureDatabases()
    configureRouting()
}