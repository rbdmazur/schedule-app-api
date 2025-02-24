package com.example.repository.model

import com.example.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Student(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val name: String,
    val info: Info
)
