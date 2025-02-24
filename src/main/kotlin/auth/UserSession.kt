package com.example.auth

import com.example.utils.UUIDSerializer
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserSession(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID
)
