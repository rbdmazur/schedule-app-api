package com.example.routes.responses

import com.example.repository.model.Study
import kotlinx.serialization.Serializable

@Serializable
data class StudiesResponse(
    val studies: List<Study>
)
