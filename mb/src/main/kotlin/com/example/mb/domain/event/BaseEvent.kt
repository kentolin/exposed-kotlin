package com.example.mb.domain.event

// shared/domain/event/BaseEvent.kt

interface BaseEvent {
    val eventId: String // Unique ID for each event
    val origin: String // Service that originated the event
}