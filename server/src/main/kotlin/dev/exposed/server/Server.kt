package dev.exposed.server

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
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
    transaction {
        Users.select(Users.name, Users.age) // Explicitly select columns
            .where { Users.age greaterEq 18 }
            .forEach {
                println("${it[Users.name]} is an adult")
            }
    }
   transaction {
       Users.update({Users.name eq "Alice"}) {
           it[age] = 26
       }
       Users.deleteWhere { Users.age.isNull() }
   }
}