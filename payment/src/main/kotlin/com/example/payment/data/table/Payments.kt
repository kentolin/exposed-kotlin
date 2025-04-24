package com.example.payment.data.table

import com.example.order.data.table.Orders
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

// Table definition
object Payments : IntIdTable() {
    val orderId = integer("order_id").references(Orders.id)
    val paymentDate = datetime("payment_date")
    val amount = double("amount")
    val paymentStatus = varchar("payment_status", 50)
}