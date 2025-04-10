package dev.exposed.server

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.transactions.transaction

fun main(){

    Database.connect("jdbc:postgresql://localhost:5432/my_db", user = "postgres", password = "root" )
    println("Connected to database")
    transaction {
        User.new {
            name = "Charlie"
            age = 30
        }

        User.all().forEach {
            println("${it.name} is ${it.age} years old")
        }
        val user = User.findById(1)
        user?.age = 11
    }
}