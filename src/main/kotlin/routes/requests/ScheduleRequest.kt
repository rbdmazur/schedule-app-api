package com.example.routes.requests

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleRequest(
    val scheduleId: String,
    val isMain: Boolean
)

@Serializable
data class ScheduleRequestList(
    val list: List<ScheduleRequest>
)
