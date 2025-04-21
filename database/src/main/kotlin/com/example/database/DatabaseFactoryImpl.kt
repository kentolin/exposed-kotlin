package com.example.database

import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


class DatabaseFactoryImpl : DatabaseFactory {
    private val config = ConfigFactory.load().getConfig("database")
    override fun init() {
        Database.connect(
            url = config.getString("url"),
            driver = config.getString("driver"),
            user = config.getString("username"),
            password = config.getString("password")
        )
    }

    override suspend fun <T> query(block: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
        block()
    }
}