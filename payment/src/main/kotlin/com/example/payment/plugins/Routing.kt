package com.example.payment.plugins


import com.example.payment.api.controller.PaymentController
import com.example.payment.api.routes.eventRouting
import com.example.payment.api.routes.paymentRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){
    // Ensure DependencyInjectionPlugin is installed
    pluginOrNull(DependencyInjectionPlugin) ?: install(DependencyInjectionPlugin)
    // Resolve Controller
    val paymentController = diContainer.resolve<PaymentController>()

    routing {
        paymentRouting(diContainer.get())
        eventRouting(diContainer.get())
    }
}