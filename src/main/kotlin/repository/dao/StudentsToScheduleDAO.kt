package com.example.repository.dao

import com.example.repository.model.Schedule
import java.util.UUID

interface StudentsToScheduleDAO {
    suspend fun addScheduleToStudent(scheduleId: Int, studentId: UUID, isMain: Boolean)
    suspend fun getSchedulesForStudent(studentId: UUID): List<Schedule>
    suspend fun deleteSchedule(scheduleId: Int, studentId: UUID): Boolean
}