```
shared/
│   ├── application/
│   │   ├── service/
│   │   │   ├── EventPublisher.kt         # Interface for publishing/subscribing events
│   │   │   ├── InMemoryEventPublisher.kt # In-memory event bus (for testing)
│   │   │   └── KafkaEventPublisher.kt   # Kafka-based event bus
│   ├── domain/
│   │   ├── dto/
│   │   │   ├── UserDTO.kt                # DTO for user
│   │   │   ├── OrderDTO.kt               # DTO for order
│   │   │   └── PaymentDTO.kt             # DTO for payment
│   │   ├── event/
│   │   │   ├── BaseEvent.kt             # Base event interface
│   │   │   ├── UserCreatedEvent.kt      # Shared event for user creation
│   │   │   ├── OrderCreatedEvent.kt     # Shared event for order creation
│   │   │   └── PaymentProcessedEvent.kt # Shared event for payment completion
```