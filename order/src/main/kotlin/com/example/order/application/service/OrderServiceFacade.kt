package com.example.order.application.service

import com.example.mb.application.service.EventPublisher
import com.example.mb.domain.dto.OrderDTO
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.order.application.mapper.OrderMapper



class OrderServiceFacade(
    private val orderService: OrderService,
    private val eventPublisher: EventPublisher
) {
    suspend fun create(dto: OrderDTO): OrderDTO {
        val savedOrder = orderService.create(dto)
        val event = OrderCreatedEvent(
            savedOrder.id,
            savedOrder.userId,
            savedOrder.totalAmount,
            savedOrder.orderDate.format(OrderMapper.formatter),
            origin = "order"
        )
        eventPublisher.publish(event)
        return savedOrder
    }
}