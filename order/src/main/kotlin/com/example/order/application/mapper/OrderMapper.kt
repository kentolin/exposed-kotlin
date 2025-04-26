package com.example.order.application.mapper

import com.example.mb.domain.dto.OrderDTO
import com.example.order.domain.model.Order
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Mapper
object OrderMapper {
    internal val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun toDTO(order: Order): OrderDTO = OrderDTO(
        order.id,
        order.userId,
        order.orderDate.format(formatter),
        order.totalAmount
    )
    fun toOrder(dto: OrderDTO): Order = Order(
        dto.id,
        dto.userId,
        LocalDateTime.parse(dto.orderDate, formatter),
        dto.totalAmount
    )
    fun toDTOs(orders: List<Order>): List<OrderDTO> = orders.map { toDTO(it) }
}
