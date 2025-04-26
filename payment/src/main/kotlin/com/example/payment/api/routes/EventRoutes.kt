package com.example.payment.api.routes

import com.example.mb.application.service.EventPublisher
import com.example.mb.domain.event.OrderCreatedEvent
import com.example.mb.domain.event.PaymentProcessedEvent
import com.example.mb.domain.event.UserCreatedEvent
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.eventRouting(eventPublisher: EventPublisher) {
    route("/events") {
        post {
            val eventJson = call.receive<String>()
            val eventType = call.request.headers["Event-Type"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Event-Type header")
            val eventId = call.request.headers["Event-Id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Event-Id header")
            val origin = call.request.headers["Origin"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Origin header")
            println("Payment: Received event $eventType (ID: $eventId, Origin: $origin, JSON: $eventJson)")
            if (origin == "payment") {
                println("Payment: Skipping event from self (ID: $eventId)")
                return@post call.respond(HttpStatusCode.Accepted)
            }
            when (eventType) {
                "UserCreatedEvent" -> {
                    val event = Json.decodeFromString<UserCreatedEvent>(eventJson)
                    println("Payment: Deserialized UserCreatedEvent: $event")
                    eventPublisher.publish(event)
                }
                "OrderCreatedEvent" -> {
                    val event = Json.decodeFromString<OrderCreatedEvent>(eventJson)
                    println("Payment: Deserialized OrderCreatedEvent: $event")
                    eventPublisher.publish(event)
                }
                "PaymentProcessedEvent" -> {
                    val event = Json.decodeFromString<PaymentProcessedEvent>(eventJson)
                    println("Payment: Deserialized PaymentProcessedEvent: $event")
                    if (event.origin == "order") {
                        println("Payment: Skipping PaymentProcessedEvent with origin=payment (ID: $eventId)")
                        return@post call.respond(HttpStatusCode.Accepted)
                    }
                    eventPublisher.publish(event)
                }
                else -> return@post call.respond(HttpStatusCode.BadRequest, "Unknown event type")
            }
            call.respond(HttpStatusCode.Accepted)
        }
    }
}