package com.example.order.application.service

import com.example.shared.domain.dto.OrderDTO

// Service interface
interface OrderService {
    suspend fun create(dto: OrderDTO): OrderDTO
    suspend fun getById(id: Int): OrderDTO?
    suspend fun update(id: Int, dto: OrderDTO): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getAll(): List<OrderDTO>
}