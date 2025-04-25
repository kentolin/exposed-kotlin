package com.example.order.plugins


import com.example.database.DatabaseFactoryImpl
import com.example.order.config.SchemaConfig
import io.ktor.server.application.*

fun Application.configureDatabases(){
    val db = DatabaseFactoryImpl()
    SchemaConfig.initialize(db)
}