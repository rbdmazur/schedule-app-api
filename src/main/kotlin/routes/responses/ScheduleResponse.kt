package com.example.routes.responses

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val schedulesItems: List<ScheduleResponseItem>
)
