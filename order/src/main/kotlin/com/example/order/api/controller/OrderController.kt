package com.example.order.api.controller

import com.example.mb.application.service.EventPublisher
import com.example.order.application.service.OrderService
import com.example.mb.domain.dto.OrderDTO
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.order.application.service.OrderServiceFacade
import com.example.order.plugins.diContainer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

// Controller
class OrderController(
    private val service: OrderService, // For get, update, delete
    private val facade: OrderServiceFacade // For create
) {
    suspend fun getAll(call: ApplicationCall) {
        call.respond(service.getAll())
    }
    suspend fun getById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        val order = service.getById(id) ?: return call.respond(HttpStatusCode.NotFound)
        call.respond(order)
    }
    suspend fun create(call: ApplicationCall) {
        val dto = call.receive<OrderDTO>()
        call.respond(HttpStatusCode.Created, facade.create(dto))
    }
    suspend fun update(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        val dto = call.receive<OrderDTO>()
        if (service.update(id, dto)) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound)
    }
    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        if (service.delete(id)) call.respond(HttpStatusCode.NoContent) else call.respond(HttpStatusCode.NotFound)
    }

}