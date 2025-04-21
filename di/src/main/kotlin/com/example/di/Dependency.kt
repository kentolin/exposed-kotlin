package com.example.di

// Represents a dependency definition
sealed class Dependency(val scope: ScopeType) {
    data class Singleton(val instance: Any) : Dependency(ScopeType.SINGLETON)
    data class Factory(val creator: (DIContainer) -> Any) : Dependency(ScopeType.FACTORY)
    data class Request(val creator: (DIContainer) -> Any) : Dependency(ScopeType.REQUEST)
}