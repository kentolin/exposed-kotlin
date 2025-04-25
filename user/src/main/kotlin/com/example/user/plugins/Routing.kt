package com.example.user.plugins

import com.example.user.api.controller.UserController
import com.example.user.api.routes.eventRouting
import com.example.user.api.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){
    // Ensure DependencyInjectionPlugin is installed
    pluginOrNull(DependencyInjectionPlugin) ?: install(DependencyInjectionPlugin)
    // Resolve Controller
    val userController = diContainer.resolve<UserController>()

    routing {
        userRouting(diContainer.get())
        eventRouting(diContainer.get())
    }
}