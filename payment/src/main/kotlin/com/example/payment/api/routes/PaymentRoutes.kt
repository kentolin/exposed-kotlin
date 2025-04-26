package com.example.payment.api.routes

import com.example.payment.api.controller.PaymentController
import io.ktor.server.routing.*

// Routing
fun Route.paymentRouting(controller: PaymentController) {
    route("/payments") {
        get { controller.getAll(call) }
        get("/{id}") { controller.getById(call) }
        post { controller.create(call) }
        put("/{id}") { controller.update(call) }
        delete("/{id}") { controller.delete(call) }

    }
}