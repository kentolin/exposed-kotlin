package com.example.order.application.service

import com.example.shared.application.service.EventPublisher
import com.example.shared.domain.dto.OrderDTO
import com.example.shared.domain.dto.UserDTO
import com.example.shared.domain.event.OrderCreatedEvent
import com.example.order.application.mapper.OrderMapper
import com.example.order.data.repository.OrderRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.client.request.*

class OrderServiceFacade(
    private val orderRepository: OrderRepository,
    private val httpClient: HttpClient,
    private val userApiUrl: String,
    private val eventPublisher: EventPublisher
) {
    suspend fun create(dto: OrderDTO): OrderDTO {
        // Validate user via UserModule API
        val userResponse = httpClient.get("$userApiUrl/${dto.userId}")
        if (userResponse.status != HttpStatusCode.OK) {
            throw IllegalArgumentException("User not found")
        }
        val userDto = userResponse.body<UserDTO>()

        // Create and save order
        val order = OrderMapper.toOrder(dto)
        val savedOrder = orderRepository.create(
            order.userId,
            order.orderDate,
            order.totalAmount
        )

        // Publish event
        eventPublisher.publish(OrderCreatedEvent(
            savedOrder.id,
            savedOrder.userId,
            savedOrder.totalAmount,
            savedOrder.orderDate.format(OrderMapper.formatter)
        ))

        return OrderMapper.toDTO(savedOrder)
    }
}