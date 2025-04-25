package com.example.user.plugins


import com.example.database.DatabaseFactoryImpl
import com.example.user.config.SchemaConfig
import io.ktor.server.application.*

fun Application.configureDatabases(){
    val db = DatabaseFactoryImpl()
    SchemaConfig.initialize(db)
}