package com.example.payment.api.controller

import com.example.payment.application.service.PaymentService
import com.example.mb.domain.dto.PaymentDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

// Controller
class PaymentController(private val service: PaymentService) {
    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(service.getAll())
    }

    suspend fun getById(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val payment = service.getById(id) ?: return ctx.respond(HttpStatusCode.NotFound)
        ctx.respond(payment)
    }

    suspend fun create(ctx: ApplicationCall) {
        val dto = ctx.receive<PaymentDTO>()
        ctx.respond(HttpStatusCode.Created, service.create(dto))
    }

    suspend fun update(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val dto = ctx.receive<PaymentDTO>()
        if (service.update(id, dto)) ctx.respond(HttpStatusCode.OK) else ctx.respond(HttpStatusCode.NotFound)
    }

    suspend fun delete(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        if (service.delete(id)) ctx.respond(HttpStatusCode.NoContent) else ctx.respond(HttpStatusCode.NotFound)
    }
}