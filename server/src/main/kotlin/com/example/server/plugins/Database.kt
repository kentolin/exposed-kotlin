package com.example.server.plugins


import com.example.database.DatabaseFactoryImpl
import com.example.server.config.SchemaConfig
import io.ktor.server.application.*

fun Application.configureDatabases(){
    val db = DatabaseFactoryImpl()
    SchemaConfig.initialize(db)
}