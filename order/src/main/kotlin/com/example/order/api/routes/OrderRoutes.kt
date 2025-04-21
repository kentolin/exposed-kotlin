package com.example.order.api.routes

import com.example.order.api.controller.OrderController
import io.ktor.server.routing.*

// Routing
fun Route.orderRouting(controller: OrderController) {
    route("/orders") {
        get { controller.getAll(call) }
        get("/{id}") { controller.getById(call) }
        post { controller.create(call) }
        put("/{id}") { controller.update(call) }
        delete("/{id}") { controller.delete(call) }
    }
}