package com.example.user.api.routes


import com.example.mb.application.service.EventPublisher
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.mb.domain.event.UserCreatedEvent
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.eventRouting(eventPublisher: EventPublisher) {
    route("/events") {
        post {
            val eventJson = call.receive<String>()
            val eventType = call.request.headers["Event-Type"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Event-Type header")
            when (eventType) {
                "UserCreatedEvent" -> {
                    val event = Json.decodeFromString<UserCreatedEvent>(eventJson)
                    eventPublisher.publish(event)
                }
                "OrderCreatedEvent" -> {
                    val event = Json.decodeFromString<OrderCreatedEvent>(eventJson)
                    eventPublisher.publish(event)
                }
                else -> return@post call.respond(HttpStatusCode.BadRequest, "Unknown event type")
            }
            call.respond(HttpStatusCode.Accepted)
        }
    }
}