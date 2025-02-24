package com.example.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val id: Int,
    val title: String,
    val shortTitle: String?,
    val infoId: Int
)
