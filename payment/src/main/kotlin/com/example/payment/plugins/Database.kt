package com.example.payment.plugins


import com.example.database.DatabaseFactoryImpl
import com.example.payment.config.SchemaConfig
import io.ktor.server.application.*

fun Application.configureDatabases(){
    val db = DatabaseFactoryImpl()
    SchemaConfig.initialize(db)
}