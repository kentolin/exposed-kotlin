package com.example.user.application.service

import com.example.mb.application.service.EventPublisher
import com.example.mb.domain.dto.UserDTO
import com.example.mb.domain.event.UserCreatedEvent
import java.util.UUID

class UserServiceFacade(
    private val userService: UserService,
    private val eventPublisher: EventPublisher
) {
    suspend fun create(dto: UserDTO): UserDTO {
        println("Facade: Creating user with DTO: $dto")
        val createdUser = userService.create(dto)
        println("Facade: User created: $createdUser")
        val event = UserCreatedEvent(
            userId = createdUser.id,
            name = createdUser.name,
            eventId = UUID.randomUUID().toString(),
            origin = "user"
        )
        println("Facade: Publishing UserCreatedEvent: $event")
        eventPublisher.publish(event)
        return createdUser
    }
}