package com.example.payment.data.entity

import com.example.payment.data.table.Payments
import com.example.payment.domain.model.Payment
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

// Entity
class PaymentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PaymentEntity>(Payments)
    var orderId by Payments.orderId
    var paymentDate by Payments.paymentDate
    var amount by Payments.amount
    var paymentStatus by Payments.paymentStatus

    fun toPayment() = Payment(id.value, orderId, paymentDate, amount, paymentStatus)
}