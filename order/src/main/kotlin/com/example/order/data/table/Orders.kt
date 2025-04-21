package com.example.order.data.table

import com.example.user.data.table.Users
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

// Table definition
object Orders : IntIdTable() {
    val userId = integer("user_id").references(Users.id)
    val orderDate = datetime("order_date")
    val totalAmount = double("total_amount")
}