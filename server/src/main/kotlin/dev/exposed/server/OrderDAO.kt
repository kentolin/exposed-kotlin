package dev.exposed.server

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderDAO>(OrderTable)

    var user by UserDAO referencedOn OrderTable.userId
    var amount by OrderTable.amount
}