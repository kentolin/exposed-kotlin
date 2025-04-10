package dev.exposed.server

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main(){

    //Database.connect("jdbc:h2:mem:test", driver = "org.h2.driver" )
    Database.connect("jdbc:postgresql://localhost:5432/my_db", user = "postgres", password = "root" )
    println("Connected to database")
    transaction {
        SchemaUtils.create(Users)
        println("Table created!")
    }
    transaction {
        Users.insert {
            it[name] = "Duke"
            it[age] = 25
        }
        Users.insert {
            it[name] = "Charles"
            it[age] = null
        }
    }
    transaction {
        Users.selectAll().forEach { row ->
            println("ID: ${row[Users.id]}, Name: ${row[Users.name]}, Age: ${row[Users.age]}")
        }
    }
}