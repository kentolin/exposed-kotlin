package dev.exposed.server

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object OrderTable : IntIdTable("orders") {
    val userId = integer("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val amount = decimal("amount", precision = 10, scale = 2)
}
