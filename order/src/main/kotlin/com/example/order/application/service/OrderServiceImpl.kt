package com.example.order.application.service

import com.example.order.application.mapper.OrderMapper
import com.example.order.data.repository.OrderRepository
import com.example.mb.domain.dto.OrderDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Service implementation
class OrderServiceImpl(
    private val repository: OrderRepository
) : OrderService {
    override suspend fun create(dto: OrderDTO): OrderDTO {
        val order = repository.create(dto.userId, LocalDateTime.parse(dto.orderDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME), dto.totalAmount)
        return OrderMapper.toDTO(order)
    }

    override suspend fun getById(id: Int): OrderDTO? {
        return repository.findById(id)?.let { OrderMapper.toDTO(it) }
    }

    override suspend fun update(id: Int, dto: OrderDTO): Boolean {
        return repository.update(id, dto.userId, LocalDateTime.parse(dto.orderDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME), dto.totalAmount)
    }

    override suspend fun delete(id: Int): Boolean {
        return repository.delete(id)
    }

    override suspend fun getAll(): List<OrderDTO> {
        return OrderMapper.toDTOs(repository.findAll())
    }
}