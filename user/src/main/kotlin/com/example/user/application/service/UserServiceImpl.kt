package com.example.user.application.service

import com.example.user.application.mapper.UserMapper
import com.example.user.data.repository.UserRepository
import com.example.mb.domain.dto.UserDTO

// Service implementation
class UserServiceImpl(
    private val repository: UserRepository
) : UserService {
    override suspend fun create(dto: UserDTO): UserDTO {
        val user = UserMapper.toUser(dto).apply {
            validate()
        }
        return UserMapper.toDTO(repository.create(user.name))
    }

    override suspend fun getById(id: Int): UserDTO? {
        return repository.findById(id)?.let { UserMapper.toDTO(it) }
    }

    override suspend fun update(id: Int, dto: UserDTO): Boolean {
        val user = UserMapper.toUser(dto).apply {
            validate()
        }
        return repository.update(id, user.name)
    }

    override suspend fun delete(id: Int): Boolean {
        return repository.delete(id)
    }

    override suspend fun getAll(): List<UserDTO> {
        return UserMapper.toDTOs(repository.findAll())
    }
}