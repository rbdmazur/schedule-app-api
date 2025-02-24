package com.example.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class Faculty(
    val id: Int,
    val title: String,
    val fullTitle: String
)
