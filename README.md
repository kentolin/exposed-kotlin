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

Business logic can be split into core business rules (domain layer) and use-case-specific rules (application layer).
Validation can occur at multiple levels:

Domain-level validation: Ensures a domain object is inherently valid (e.g., Order.isValidAmount).

Application-level validation: Ensures the use case is valid (e.g., "a user must exist before placing an order").

In simple applications, the application layer may directly invoke domain-level validations, blurring the lines.

#### Where Should Business Logic Reside?
##### Core Business Logic: 
Belongs in the domain layer. These are rules that define the business entities and are true regardless of the use case. Examples:
User.isValidEmail: An email must contain @ to be a valid user.
Order.isValidAmount: An order amount must be positive.
##### Use-Case-Specific Logic: 
Belongs in the application layer. These are rules or validations specific to a particular workflow or use case. Examples:
Checking if a user exists before placing an order (userRepository.findById).
Ensuring a user has sufficient balance (if applicable) before placing an order.
Why Validation Appears in Application Layer:
In the example, OrderServiceFacade calls user.isValidEmail() and order.isValidAmount() to enforce domain rules as part of the "place order" use case. This is not the application layer defining the rules but invoking domain-layer validations.
The application layer also adds use-case-specific validation, like checking if the user exists (userRepository.findById(dto.userId) ?: throw ...).
#### Correct Placement
##### Domain Layer: Define validation and business logic in domain objects or domain services.
Example: User.isValidEmail, Order.isValidAmount.
##### Application Layer: Orchestrate the use case, invoke domain validations, and add use-case-specific checks.
Example: Ensure the user exists, call isValidEmail, and save the order.

### Data Access Layer:
Manages database interactions, including table objects, entity classes, and repositories, mapping domain objects to database entities.

## Summary

- Domain Layer: Owns core business entities and rules (e.g., User, Order, isValidEmail).
- Application Layer: Orchestrates use cases, coordinates with repositories and other modules, and handles DTOs (e.g., OrderServiceFacade).
- Business logic belongs in the domain layer; the application layer invokes these rules and adds use-case-specific logic (e.g., checking user existence).
- Validation: Domain validations (e.g., isValidEmail) are defined in the domain layer and called by the application layer during use-case execution.
- Module Interactions: Use facades, domain events, or APIs to manage interactions, with dependency injection for wiring and events for decoupling.

## Overview of Components

### Table Object:
Defined using Exposed’s Table or IntIdTable to represent a database table.
Maps to the database schema (columns, constraints).
Lives in the data access layer.
Example: Users table with columns id, name, email.
### Entity Class:
An Exposed class extending Entity that maps rows in a Table Object to objects.
Provides an object-oriented way to interact with the database (e.g., create, update, delete).
Lives in the data access layer.
Example: UserEntity tied to the Users table.
### Domain Object:
A plain Kotlin class (often a data class) representing a business entity.
Contains business logic (e.g., validation, calculations).
Lives in the domain layer, independent of the database or framework.
Example: User with business logic like email validation.
- Domain Events: Notify the system of significant changes (e.g., OrderPlaced).
- Domain Objects: Represent business concepts (Entities like Customer, Value Objects like Address).
- Domain Services: Handle business logic that spans multiple objects (e.g., PaymentProcessingService).
- Domain Layer: The architectural layer containing all domain-related components, isolated from technical concerns.


### Data Transfer Object (DTO):
A simple class used to transfer data between layers or systems (e.g., API request/response).
Contains no business logic, only data fields.
Lives in the application layer or API layer.
Example: UserDto for API responses, excluding sensitive fields.