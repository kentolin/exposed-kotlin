package com.example.payment.data.repository

import com.example.database.DatabaseFactory
import com.example.payment.data.entity.PaymentEntity
import com.example.payment.domain.model.Payment
import java.time.LocalDateTime

// Repository implementation
class PaymentRepositoryImpl(private val db: DatabaseFactory) : PaymentRepository {
    init {
        db.init()
    }

    override suspend fun create(orderId: Int, paymentDate: LocalDateTime, amount: Double, paymentStatus: String): Payment = db.query {
        val entity = PaymentEntity.new {
            this.orderId = orderId
            this.paymentDate = paymentDate
            this.amount = amount
            this.paymentStatus = paymentStatus
        }
        entity.toPayment()
    }

    override suspend fun findById(id: Int): Payment? = db.query {
        PaymentEntity.findById(id)?.toPayment()
    }

    override suspend fun update(id: Int, orderId: Int, paymentDate: LocalDateTime, amount: Double, paymentStatus: String): Boolean = db.query {
        val entity = PaymentEntity.findById(id)
        if (entity != null) {
            entity.orderId = orderId
            entity.paymentDate = paymentDate
            entity.amount = amount
            entity.paymentStatus = paymentStatus
            true
        } else {
            false
        }
    }

    override suspend fun delete(id: Int): Boolean = db.query {
        val entity = PaymentEntity.findById(id)
        if (entity != null) {
            entity.delete()
            true
        } else {
            false
        }
    }

    override suspend fun findAll(): List<Payment> = db.query {
        PaymentEntity.all().map { it.toPayment() }
    }
}