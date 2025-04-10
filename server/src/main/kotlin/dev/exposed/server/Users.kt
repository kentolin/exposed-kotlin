package dev.exposed.server

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Users : IntIdTable("users") {
    val name = varchar("name", 50)
    val age = integer("age").nullable()
}
