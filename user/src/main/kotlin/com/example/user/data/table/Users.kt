package com.example.user.data.table

import org.jetbrains.exposed.dao.id.IntIdTable


// Table definition
object Users : IntIdTable() {
    val name = varchar("name", 255)
}