package com.example.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val id: Int,
    val facultyId: Int,
    val specialization: String,
    val course: Int,
    val group: Int
)
