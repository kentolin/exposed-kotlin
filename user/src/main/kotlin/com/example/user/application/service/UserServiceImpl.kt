package com.example.user.application.service

import com.example.user.application.mapper.UserMapper
import com.example.user.data.repository.UserRepository
import com.example.shared.domain.dto.UserDTO

// Service implementation
class UserServiceImpl(
    private val repository: UserRepository
) : UserService {
    override suspend fun create(dto: UserDTO): UserDTO {
        val user = repository.create(dto.name)
        return UserMapper.toDTO(user)
    }

    override suspend fun getById(id: Int): UserDTO? {
        return repository.findById(id)?.let { UserMapper.toDTO(it) }
    }

    override suspend fun update(id: Int, dto: UserDTO): Boolean {
        return repository.update(id, dto.name)
    }

    override suspend fun delete(id: Int): Boolean {
        return repository.delete(id)
    }

    override suspend fun getAll(): List<UserDTO> {
        return UserMapper.toDTOs(repository.findAll())
    }
}