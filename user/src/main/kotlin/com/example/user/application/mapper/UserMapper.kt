package com.example.user.application.mapper

import com.example.shared.domain.dto.UserDTO
import com.example.user.domain.model.User

// Mapper
object UserMapper {
    fun toDTO(user: User): UserDTO = UserDTO(user.id, user.name)
    fun toUser(dto: UserDTO): User = User(dto.id, dto.name)
    fun toDTOs(users: List<User>): List<UserDTO> = users.map { toDTO(it) }
}