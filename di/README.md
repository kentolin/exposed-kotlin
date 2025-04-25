```
di/
│   ├── src/main/kotlin/com/example/di/
│   │   ├── core/
│   │   │   ├── DIContainer.kt
│   │   │   ├── Dependency.kt
│   │   │   └── ScopeType.kt
│   │   ├── dsl/
│   │   │   └── Module.kt
│   │   ├── exception/
│   │   │   └── DIException.kt
│   │   └── internal/
│   │       └── ConstructorMetadata.kt
│   ├── src/test/kotlin/dev/exposed/di/
│   │   └── core/
│   │       └── DIContainerTest.kt
│   └── build.gradle.kts
```

## Package Structure: com.example.di

### core/:

Contains the core DI components:

DIContainer.kt: The main DI container class, handling registration and resolution.

Dependency.kt: Sealed class for dependency types (Singleton, Factory, Request).

ScopeType.kt: Enum defining scope types (SINGLETON, FACTORY, REQUEST).

Why?: Groups the primary functionality, making it clear where the core logic resides.

### dsl/:

Contains Module.kt: The DSL for defining dependency modules.

Why?: Isolates the DSL to emphasize its role as a user-facing API for configuring dependencies.

### exception/:

Contains DIException.kt: Custom exceptions for DI errors (e.g., DependencyNotFound, CircularDependency).

Why?: Separates error handling, making it easy to extend with new exception types.

### internal/:

Contains ConstructorMetadata.kt: Internal metadata for constructor injection.

Why?: Marks ConstructorMetadata as internal to the DI framework, hiding implementation details from users.
src/test/kotlin/:

Contains tests (e.g., DIContainerTest.kt) to verify DI functionality.

Why?: Ensures the DI framework is well-tested, with tests colocated in the module.

Why This Structure?

Clarity: Packages (core, dsl, exception, internal) group related components, making it easy to navigate.

Extensibility: New features (e.g., annotation-based injection) can be added in new subpackages (e.g., annotation).

Encapsulation: internal hides implementation details, exposing only public APIs (DIContainer, Module).

Testability: Dedicated test directory supports unit testing the DI framework independently.