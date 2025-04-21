package com.example.user.application.service

import com.example.user.domain.dto.UserDTO

// Service interface
interface UserService {
    suspend fun create(dto: UserDTO): UserDTO
    suspend fun getById(id: Int): UserDTO?
    suspend fun update(id: Int, dto: UserDTO): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getAll(): List<UserDTO>
}