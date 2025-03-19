package com.example.routes.responses

import com.example.repository.model.Schedule
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleSimpleResponse(
    val schedules: List<Schedule>
)
