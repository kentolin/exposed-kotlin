package dev.exposed.server.plugins

import dev.exposed.server.UserDAO
import dev.exposed.server.UserDTO
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.configureRouting(){
    routing {
        staticResources("static", "static")
        get("/") {
            call.respondText("Hello World!!")
        }

    }
}