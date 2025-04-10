package dev.exposed.server

import org.jetbrains.exposed.sql.Database

fun main(){
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
    println("Connected to database")
}