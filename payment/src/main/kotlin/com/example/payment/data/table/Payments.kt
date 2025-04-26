package com.example.payment.data.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

// Table definition
object Payments : IntIdTable() {
    val orderId = integer("order_id")
    val paymentDate = datetime("payment_date")
    val amount = double("amount")
    val paymentStatus = varchar("payment_status", 50)
}