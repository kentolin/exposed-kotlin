package com.example.order.data.repository

import com.example.database.DatabaseFactory
import com.example.order.data.entity.OrderEntity
import com.example.order.domain.model.Order
import java.time.LocalDateTime

// Repository implementation
class OrderRepositoryImpl(private val db: DatabaseFactory) : OrderRepository {
    init {
        db.init()
    }
    override suspend fun create(userId: Int, orderDate: LocalDateTime, totalAmount: Double): Order = db.query {
        val entity = OrderEntity.new {
            this.userId = userId
            this.orderDate = orderDate
            this.totalAmount = totalAmount
        }
        entity.toOrder()
    }

    override suspend fun findById(id: Int): Order? = db.query {
        OrderEntity.findById(id)?.toOrder()
    }

    override suspend fun update(id: Int, userId: Int, orderDate: LocalDateTime, totalAmount: Double): Boolean = db.query {
        val entity = OrderEntity.findById(id)
        if (entity != null) {
            entity.userId = userId
            entity.orderDate = orderDate
            entity.totalAmount = totalAmount
            true
        } else {
            false
        }
    }

    override suspend fun delete(id: Int): Boolean = db.query {
        val entity = OrderEntity.findById(id)
        if (entity != null) {
            entity.delete()
            true
        } else {
            false
        }
    }

    override suspend fun findAll(): List<Order> = db.query {
        OrderEntity.all().map { it.toOrder() }
    }
}