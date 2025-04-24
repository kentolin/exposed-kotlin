```
common/
│   ├── application/
│   │   ├── service/
│   │   │   ├── EventPublisher.kt         # Interface for publishing events
│   │   │   └── InMemoryEventPublisher.kt # In-memory event bus implementation
│   └── domain/
│       ├── event/
│       │   ├── BaseEvent.kt             # Optional base event interface/class
├── user/
```