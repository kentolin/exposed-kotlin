package dev.exposed.server

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("users") {
    val name = varchar("name", 50)
    val age = integer("age").nullable()
}
