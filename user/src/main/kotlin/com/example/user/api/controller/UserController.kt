package com.example.user.api.controller

import com.example.user.application.service.UserService
import com.example.mb.domain.dto.UserDTO
import com.example.user.application.service.UserServiceFacade
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*



// Controller
class UserController(
    private val service: UserService, // For get, update, delete
    private val facade: UserServiceFacade // For create
) {
    suspend fun getAll(call: ApplicationCall) {
        call.respond(service.getAll())
    }
    suspend fun getById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        val user = service.getById(id) ?: return call.respond(HttpStatusCode.NotFound)
        call.respond(user)
    }
    suspend fun create(call: ApplicationCall) {
        val dto = call.receive<UserDTO>()
        call.respond(HttpStatusCode.Created, facade.create(dto)) // Use facade for creation
    }
    suspend fun update(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        val dto = call.receive<UserDTO>()
        if (service.update(id, dto)) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound)
    }
    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        if (service.delete(id)) call.respond(HttpStatusCode.NoContent) else call.respond(HttpStatusCode.NotFound)
    }
}