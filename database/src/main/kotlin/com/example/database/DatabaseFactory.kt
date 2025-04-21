package com.example.database

import org.jetbrains.exposed.sql.Transaction

// Database Factory
interface DatabaseFactory {
    fun init()
    suspend fun <T> query(block: suspend Transaction.() -> T): T
}
