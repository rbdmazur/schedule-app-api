package com.example.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val id: Int,
    val title: String,
    val lastUpdate: Long,
    val infoId: Int
)
