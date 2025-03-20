package com.example.service

import com.example.repository.model.Schedule
import com.example.repository.model.Study
import com.example.repository.model.Subject
import com.example.repository.model.Teacher
import com.example.repository.repositories.ScheduleRepository
import com.example.routes.responses.ScheduleResponseItem
import com.example.utils.DaysOfWeek
import com.example.utils.TypeOfStudy
import java.util.UUID
import javax.inject.Inject

class ScheduleService @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    //Subject
    suspend fun getAllSubjects(): List<Subject> =
        scheduleRepository.getAllSubjects()

    suspend fun updateSubject(subject: Subject) {
        scheduleRepository.updateSubject(subject)
    }

    suspend fun deleteSubject(id: Int): Boolean =
        scheduleRepository.deleteSubject(id)

    suspend fun getSubjectById(id: Int): Subject? =
        scheduleRepository.getSubjectById(id)

    suspend fun getSubjectsByInfo(infoId: Int): List<Subject> =
        scheduleRepository.getSubjectsByInfo(infoId)

    suspend fun addSubject(
        title: String,
        short: String,
        info: Int
    ) : Subject =
        scheduleRepository.addSubject(title, short, info)

    //Schedule
    suspend fun getAllSchedules(): List<Schedule> =
        scheduleRepository.getAllSchedules()

    suspend fun addSchedule(title: String, info: Int) =
        scheduleRepository.addSchedule(title, info)

    suspend fun updateSchedule(schedule: Schedule) {
        scheduleRepository.updateSchedule(schedule)
    }

    suspend fun deleteSchedule(id: Int): Boolean =
        scheduleRepository.deleteSchedule(id)

    suspend fun getScheduleById(id: Int): Schedule? =
        scheduleRepository.getScheduleById(id)

    suspend fun getScheduleByInfo(infoId: Int): Schedule? =
        scheduleRepository.getScheduleByInfo(infoId)

    suspend fun getSchedulesForFaculty(facultyId: Int): List<Schedule> =
        scheduleRepository.getSchedulesForFaculty(facultyId)

    suspend fun getSchedulesForCourse(facultyId: Int, course: Int): List<Schedule> =
        scheduleRepository.getSchedulesForCourse(facultyId, course)

    //Study
    suspend fun getAllStudies(): List<Study> =
        scheduleRepository.getAllStudies()

    suspend fun addStudy(
        subject: Subject,
        day: DaysOfWeek,
        number: Int,
        type: TypeOfStudy,
        time: String,
        auditorium: String,
        teacher: Teacher,
        scheduleId: Int
    ): Study =
        scheduleRepository.addStudy(
            subject = subject,
            day = day,
            number = number,
            type = type,
            time = time,
            auditorium = auditorium,
            teacher = teacher,
            scheduleId = scheduleId
        )

    suspend fun updateStudy(study: Study) {
        scheduleRepository.updateStudy(study)
    }

    suspend fun deleteStudy(id: Int): Boolean =
        scheduleRepository.deleteStudy(id)

    suspend fun getStudyById(id: Int): Study? =
        scheduleRepository.getStudyById(id)

    suspend fun getStudiesForScheduleId(scheduleId: Int): List<Study> =
        scheduleRepository.getStudiesForScheduleId(scheduleId)

    suspend fun getStudiesForScheduleByDay(scheduleId: Int, day: DaysOfWeek): List<Study> =
        scheduleRepository.getStudiesForScheduleByDay(scheduleId, day)

    suspend fun getStudiesForTeacherByDay(teacherId: UUID, day: DaysOfWeek): List<Study> =
        scheduleRepository.getStudiesForTeacherByDay(teacherId, day)

    //StudentToSchedule
    suspend fun addScheduleToStudent(scheduleId: Int, studentId: UUID, isMain: Boolean) {
        scheduleRepository.addScheduleToStudent(scheduleId, studentId, isMain)
    }

    suspend fun deleteScheduleFromStudent(scheduleId: Int, studentId: UUID): Boolean =
        scheduleRepository.deleteScheduleFromStudent(scheduleId, studentId)

    suspend fun getSchedulesForStudent(studentId: UUID): List<ScheduleResponseItem> =
        scheduleRepository.getSchedulesForStudent(studentId)

    suspend fun getSchedulesByFilter(userId: UUID, facultyId: Int?, course: Int?, group: Int?): List<Schedule> =
        scheduleRepository.getSchedulesByFilter(userId, facultyId, course, group)
}