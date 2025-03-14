package com.example.routes.responses

import com.example.repository.model.Schedule
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponseItem(
    val schedule: Schedule,
    val isMain: Boolean
)
