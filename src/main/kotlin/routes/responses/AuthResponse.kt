package com.example.routes.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
