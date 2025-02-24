package com.example.repository.model

import com.example.utils.DaysOfWeek
import com.example.utils.TypeOfStudy
import kotlinx.serialization.Serializable

@Serializable
data class Study(
    val id: Int,
    val subject: Subject,
    val day: DaysOfWeek,
    val number: Int,
    val time: String,
    val type: TypeOfStudy,
    val auditorium: String,
    val teacher: Teacher,
    val scheduleId: Int
)
