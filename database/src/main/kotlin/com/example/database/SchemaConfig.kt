package com.example.database

// Schema Configuration
object SchemaConfig {
    fun initialize(db: DatabaseFactory) {
        db.init()
        db.initSchema()
    }
}