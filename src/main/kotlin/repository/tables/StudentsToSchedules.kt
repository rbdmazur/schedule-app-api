package com.example.repository.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object StudentsToSchedules : Table("students_to_schedules") {
    val scheduleId = reference("schedule_id", Schedules.id, ReferenceOption.CASCADE)
    val studentId = reference("student_id", Students.userId, ReferenceOption.CASCADE)
    val isMain: Column<Boolean> = bool("is_main").default(false)

    override val primaryKey: PrimaryKey = PrimaryKey(scheduleId, studentId)
}