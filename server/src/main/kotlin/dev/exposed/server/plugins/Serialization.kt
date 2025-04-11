package dev.exposed.server.plugins

import dev.exposed.server.UserDAO
import dev.exposed.server.UserDTO
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.configureSerialization(){
    install(ContentNegotiation) {
        json()
    }
    routing {

        // Create
        post("/users"){
            val userDTO = call.receive<UserDTO>()
            val user = newSuspendedTransaction(Dispatchers.IO) {
                UserDAO.new {
                    name = userDTO.name
                    age = userDTO.age
                }
            }
            call.respond(HttpStatusCode.Created, UserDTO(user.id.value, user.name, user.age))
        }

        // Read All
        get("/users"){
            val users = newSuspendedTransaction(Dispatchers.IO) {
                UserDAO.all().map { UserDTO(it.id.value, it.name, it.age) }
            }
            call.respond(users)
        }

        // Read One
        get("/users/{id}"){
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val user = newSuspendedTransaction(Dispatchers.IO) {
                UserDAO.findById(id)?.let {UserDTO(it.id.value, it.name, it.age)}

            } ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(user)
        }

        // Update
        put("/users/{id}"){
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val userDTO = call.receive<UserDTO>()
            val updated = newSuspendedTransaction(Dispatchers.IO) {
                UserDAO.findById(id)?.apply {
                    name = userDTO.name
                    age = userDTO.age
                } != null

            }
            if(updated)
                call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound)
        }

        // Delete
        delete("/users/{id}"){
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val deleted = newSuspendedTransaction(Dispatchers.IO) {
                UserDAO.findById(id)?.delete() != null

            }
            if(deleted)
                call.respond(HttpStatusCode.NoContent) else call.respond(HttpStatusCode.NotFound)
        }

    }
}