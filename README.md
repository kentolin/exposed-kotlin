```
[Client]
| Sends/receives DTOs (UserDto, OrderDto)
v
[API Layer]
| Passes DTOs to Facade
v
[Application Layer: OrderServiceFacade]
| Converts DTOs <-> Domain Objects
| Validates business logic (User, Order)
| Calls Repositories
v
[Domain Layer: User, Order]
| Contains business logic (isValidEmail, isValidAmount)
| Passed to/from Repositories
v
[Data Access Layer: UserRepository, OrderRepository]
| Maps Domain Objects <-> Entity Classes
| Interacts with Entity Classes/Table Objects
v
[Data Access Layer: UserEntity, OrderEntity]
| Maps to rows in Table Objects
| Performs CRUD operations
v
[Database: Users, Orders]
| Stores data
```

## Architectural Layers


### API Layer:
Handles HTTP requests/responses, processes DTOs, and interacts with the application layer (e.g., controllers, routing).

### Application Layer:
Orchestrates business logic, converts DTOs to domain objects, and coordinates with repositories or services (e.g., services, mappers).

### Domain Layer:
Contains domain objects with business logic, independent of persistence or frameworks.

### Data Access Layer:
Manages database interactions, including table objects, entity classes, and repositories, mapping domain objects to database entities.

## Key Points

### Table Objects 
define the database schema and are used by Entity classes.

### Entity Classes 
handle database operations and are tied to Table objects.

### Domain Objects 
encapsulate business logic and are mapped to/from Entity classes by repositories.

### DTOs 
facilitate data transfer and are converted to/from domain objects by the facade.

### Facade 
coordinates the flow, ensuring separation of concerns.

### Repositories 
bridge the domain and data access layers, keeping the domain layer independent of Exposed.

This implementation follows clean architecture principles, ensuring maintainability and testability.