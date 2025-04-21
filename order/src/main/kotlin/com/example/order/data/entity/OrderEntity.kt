package com.example.order.data.entity

import com.example.order.data.table.Orders
import com.example.order.domain.model.Order
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

// Entity
class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(Orders)
    var userId by Orders.userId
    var orderDate by Orders.orderDate
    var totalAmount by Orders.totalAmount

    fun toOrder() = Order(id.value, userId, orderDate, totalAmount)
}