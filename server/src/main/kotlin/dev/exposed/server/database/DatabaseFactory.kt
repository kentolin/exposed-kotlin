package dev.exposed.server.database

import com.typesafe.config.ConfigFactory
import dev.exposed.server.db.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


// Database Factory
interface DatabaseFactory {
    fun init()
    fun initSchema()
    suspend fun <T> query(block: suspend Transaction.() -> T): T
}

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
    override fun initSchema() {
        transaction { SchemaUtils.create(Users) }
    }

    override suspend fun <T> query(block: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
        block()
    }
}

// Schema Configuration
object SchemaConfig {
    fun initialize(db: DatabaseFactory) {
        db.init()
        db.initSchema()
    }
}