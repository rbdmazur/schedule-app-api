package com.example.routes.responses

import com.example.repository.model.Faculty
import kotlinx.serialization.Serializable

@Serializable
data class FacultyResponse(
    val faculties: List<Faculty>
)
