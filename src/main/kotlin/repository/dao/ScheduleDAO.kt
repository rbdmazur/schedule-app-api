package com.example.repository.dao

import com.example.repository.model.Schedule
import java.util.*


interface ScheduleDAO {
    suspend fun getAllSchedules(): List<Schedule>
    suspend fun addSchedule(title: String, info: Int) : Schedule
    suspend fun updateSchedule(schedule: Schedule)
    suspend fun deleteSchedule(id: Int): Boolean

    suspend fun getScheduleById(id: Int): Schedule?
    suspend fun getScheduleByInfo(infoId: Int): Schedule?
    suspend fun getSchedulesForFaculty(facultyId: Int): List<Schedule>
    suspend fun getSchedulesForCourse(facultyId: Int, course: Int): List<Schedule>
    suspend fun getSchedulesByFilter(userId: UUID, facultyId: Int?, course: Int?, group: Int?): List<Schedule>
}