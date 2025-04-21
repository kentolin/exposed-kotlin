package com.example.user.data.repository

import com.example.database.DatabaseFactory
import com.example.user.data.entity.UserEntity
import com.example.user.domain.model.User

// Repository implementation
class UserRepositoryImpl(private val db: DatabaseFactory) : UserRepository {
    init {
        db.init()
    }
    override suspend fun create(name: String): User = db.query {
        val entity = UserEntity.new {
            this.name = name
        }
        entity.toUser()
    }

    override suspend fun findById(id: Int): User? = db.query {
        UserEntity.findById(id)?.toUser()
    }

    override suspend fun update(id: Int, name: String): Boolean = db.query {
        val entity = UserEntity.findById(id)
        if (entity != null) {
            entity.name = name
            true
        } else {
            false
        }
    }

    override suspend fun delete(id: Int): Boolean = db.query {
        val entity = UserEntity.findById(id)
        if (entity != null) {
            entity.delete()
            true
        } else {
            false
        }
    }

    override suspend fun findAll(): List<User> = db.query {
        UserEntity.all().map { it.toUser() }
    }
}