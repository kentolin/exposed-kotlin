package com.example.user.api.controller

import com.example.user.application.service.UserService
import com.example.shared.domain.dto.UserDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*



// Controller
class UserController(private val service: UserService) {
    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(service.getAll())
    }
    suspend fun getById(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val user = service.getById(id) ?: return ctx.respond(HttpStatusCode.NotFound)
        ctx.respond(user)
    }
    suspend fun create(ctx: ApplicationCall) {
        val dto = ctx.receive<UserDTO>()
        ctx.respond(HttpStatusCode.Created, service.create(dto))
    }
    suspend fun update(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val dto = ctx.receive<UserDTO>()
        if (service.update(id, dto)) ctx.respond(HttpStatusCode.OK) else ctx.respond(HttpStatusCode.NotFound)
    }
    suspend fun delete(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        if (service.delete(id)) ctx.respond(HttpStatusCode.NoContent) else ctx.respond(HttpStatusCode.NotFound)
    }
}