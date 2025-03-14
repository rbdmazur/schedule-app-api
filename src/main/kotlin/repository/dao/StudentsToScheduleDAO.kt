package com.example.repository.dao

import com.example.routes.responses.ScheduleResponseItem
import java.util.UUID

interface StudentsToScheduleDAO {
    suspend fun addScheduleToStudent(scheduleId: Int, studentId: UUID, isMain: Boolean)
    suspend fun getSchedulesForStudent(studentId: UUID): List<ScheduleResponseItem>
    suspend fun deleteScheduleFromStudent(scheduleId: Int, studentId: UUID): Boolean
}