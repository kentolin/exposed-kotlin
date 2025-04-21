package com.example.database

import org.jetbrains.exposed.sql.Transaction

// Database Factory
interface DatabaseFactory {
    fun init()
    fun initSchema()
    suspend fun <T> query(block: suspend Transaction.() -> T): T
}
