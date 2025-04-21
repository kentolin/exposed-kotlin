package com.example.order.api.controller

import com.example.order.application.service.OrderService
import com.example.order.domain.dto.OrderDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

// Controller
class OrderController(private val service: OrderService) {
    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(service.getAll())
    }
    suspend fun getById(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val order = service.getById(id) ?: return ctx.respond(HttpStatusCode.NotFound)
        ctx.respond(order)
    }
    suspend fun create(ctx: ApplicationCall) {
        val dto = ctx.receive<OrderDTO>()
        ctx.respond(HttpStatusCode.Created, service.create(dto))
    }
    suspend fun update(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val dto = ctx.receive<OrderDTO>()
        if (service.update(id, dto)) ctx.respond(HttpStatusCode.OK) else ctx.respond(HttpStatusCode.NotFound)
    }
    suspend fun delete(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        if (service.delete(id)) ctx.respond(HttpStatusCode.NoContent) else ctx.respond(HttpStatusCode.NotFound)
    }
}