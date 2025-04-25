package com.example.user.application.service

import com.example.mb.application.service.EventPublisher
import com.example.mb.domain.dto.UserDTO
import com.example.mb.domain.event.UserCreatedEvent

class UserServiceFacade(
    private val userService: UserService,
    private val eventPublisher: EventPublisher
) {
    suspend fun create(dto: UserDTO): UserDTO {
        val createdUser = userService.create(dto)
        eventPublisher.publish(UserCreatedEvent(createdUser.id, createdUser.name))
        return createdUser
    }
}