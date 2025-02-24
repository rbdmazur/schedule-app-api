package com.example.repository.dao

import com.example.repository.model.Study
import com.example.repository.model.Subject
import com.example.repository.model.Teacher
import com.example.utils.DaysOfWeek
import com.example.utils.TypeOfStudy

interface StudyDAO {
    suspend fun getAllStudies(): List<Study>
    suspend fun addStudy(
        subject: Subject,
        day: DaysOfWeek,
        number: Int,
        type: TypeOfStudy,
        time: String,
        auditorium: String,
        teacher: Teacher,
        scheduleId: Int
        ) : Study
    suspend fun updateStudy(study: Study)
    suspend fun deleteStudy(id: Int): Boolean

    suspend fun getStudyById(id: Int): Study?
    suspend fun getStudiesForScheduleId(scheduleId: Int): List<Study>
    suspend fun getStudiesForScheduleByDay(scheduleId: Int, day: DaysOfWeek): List<Study>
//    suspend fun getStudiesForTeacherByDay(teacherId: Int, day: DaysOfWeek): List<Study>
}