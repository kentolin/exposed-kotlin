package com.example.server.plugins


import com.example.database.DatabaseFactoryImpl
import com.example.database.SchemaConfig
import io.ktor.server.application.*

fun Application.configureDatabases(){
    val db = DatabaseFactoryImpl()
   // SchemaConfig.initialize(db)
}