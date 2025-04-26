package com.example.payment.api.controller

import com.example.mb.application.service.EventPublisher
import com.example.payment.application.service.PaymentService
import com.example.mb.domain.dto.PaymentDTO
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.payment.application.service.PaymentServiceFacade
import com.example.payment.plugins.diContainer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

// Controller
class PaymentController(
    private val service: PaymentService, // For get, update, delete
    private val facade: PaymentServiceFacade // For create
) {
    suspend fun getAll(call: ApplicationCall) {
        call.respond(service.getAll())
    }

    suspend fun getById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        val payment = service.getById(id) ?: return call.respond(HttpStatusCode.NotFound)
        call.respond(payment)
    }

    suspend fun create(call: ApplicationCall) {
        val dto = call.receive<PaymentDTO>()
        call.respond(HttpStatusCode.Created, facade.create(dto))
    }

    suspend fun update(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        val dto = call.receive<PaymentDTO>()
        if (service.update(id, dto)) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound)
    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        if (service.delete(id)) call.respond(HttpStatusCode.NoContent) else call.respond(HttpStatusCode.NotFound)
    }

}