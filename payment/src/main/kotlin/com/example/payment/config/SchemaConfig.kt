package com.example.payment.config

import com.example.database.DatabaseFactory
import com.example.payment.data.table.Payments
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


// Schema Configuration
object SchemaConfig {
    fun initialize(db: DatabaseFactory) {
        db.init()
        transaction { SchemaUtils.create(Payments) }
    }
}