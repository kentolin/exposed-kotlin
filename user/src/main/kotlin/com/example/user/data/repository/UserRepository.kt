package com.example.user.data.repository

import com.example.user.domain.model.User

// Repository interface
interface UserRepository {
    suspend fun create(name: String): User
    suspend fun findById(id: Int): User?
    suspend fun update(id: Int, name: String): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun findAll(): List<User>
}