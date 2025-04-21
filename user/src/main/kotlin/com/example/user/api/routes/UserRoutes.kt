package com.example.user.api.routes

import com.example.user.api.controller.UserController
import io.ktor.server.routing.*

// Routing
fun Route.userRouting(controller: UserController) {
    route("/users") {
        get { controller.getAll(call) }
        get("/{id}") { controller.getById(call) }
        post { controller.create(call) }
        put("/{id}") { controller.update(call) }
        delete("/{id}") { controller.delete(call) }
    }
}