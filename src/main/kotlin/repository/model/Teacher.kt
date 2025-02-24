package com.example.repository.model

import com.example.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Teacher(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val name: String,
    val academicTitle: String?,
    val facultyId: Int
)
