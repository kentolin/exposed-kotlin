package dev.exposed.server

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val age = integer("age").nullable()

    override val primaryKey = PrimaryKey(id)
}