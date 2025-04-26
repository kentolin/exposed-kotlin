package com.example.order.plugins


import com.example.order.api.controller.OrderController
import com.example.order.api.routes.eventRouting
import com.example.order.api.routes.orderRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){
    // Ensure DependencyInjectionPlugin is installed
    pluginOrNull(DependencyInjectionPlugin) ?: install(DependencyInjectionPlugin)
    // Resolve Controller
    val orderController = diContainer.resolve<OrderController>()

    routing {
        orderRouting(diContainer.get())
        eventRouting(diContainer.get())
    }
}