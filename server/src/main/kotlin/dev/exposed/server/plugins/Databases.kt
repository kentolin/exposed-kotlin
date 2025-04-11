package dev.exposed.server.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.exposed.server.OrderTable
import dev.exposed.server.UserDAO
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases(){
    val config =  HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/my_db"
        driverClassName = "org.postgresql.Driver"
        username = "postgres"
        password = "root"
        maximumPoolSize = 10
    }
  //  val dataSource = HikariDataSource(config)
   // Database.connect(dataSource)
    Database.connect("jdbc:postgresql://localhost:5432/my_db", user = "postgres", password = "root" )
    println("Connected to database")
  /*  transaction {
        SchemaUtils.create(OrderTable)
        println("Table created")
    }
    transaction {
        UserDAO.new {
            name = "Charlie"
            age = 30
        }

        UserDAO.all().forEach {
            println("${it.name} is ${it.age} years old")
        }
        val user = UserDAO.findById(1)
        user?.age = 11
    }*/
}