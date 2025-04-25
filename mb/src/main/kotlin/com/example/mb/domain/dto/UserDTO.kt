package com.example.mb.domain.dto

import kotlinx.serialization.Serializable

// DTO for serialization
@Serializable
data class UserDTO(
    val id: Int,
    val name: String
)