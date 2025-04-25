package com.example.user.config

import com.example.database.DatabaseFactory
import com.example.user.data.table.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


// Schema Configuration
object SchemaConfig {
    fun initialize(db: DatabaseFactory) {
        db.init()
        transaction { SchemaUtils.create(Users) }

    }
}