package com.example.repository.repositories

import com.example.repository.dao.ScheduleDAO
import com.example.repository.dao.StudentsToScheduleDAO
import com.example.repository.dao.StudyDAO
import com.example.repository.dao.SubjectDAO
import com.example.repository.dbQuery
import com.example.repository.model.Schedule
import com.example.repository.model.Study
import com.example.repository.model.Subject
import com.example.repository.model.Teacher
import com.example.repository.tables.*
import com.example.utils.DaysOfWeek
import com.example.utils.TypeOfStudy
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.Instant
import java.util.*

class ScheduleRepository : SubjectDAO, ScheduleDAO, StudyDAO, StudentsToScheduleDAO {
    override suspend fun getAllSubjects(): List<Subject> =
        dbQuery {
            Subjects.selectAll().map { rowToSubject(it) }.requireNoNulls()
        }

    override suspend fun updateSubject(subject: Subject) {
        dbQuery {
            Subjects.update({ Subjects.id.eq(subject.id) }) {
                it[id] = subject.id
                it[title] = subject.title
                it[shortTitle] = subject.shortTitle
                it[infoId] = subject.infoId
            }
        }
    }

    override suspend fun deleteSubject(id: Int): Boolean =
        dbQuery {
            Subjects.deleteWhere { Subjects.id eq id } == 1
        }


    override suspend fun getSubjectById(id: Int): Subject? =
        dbQuery {
            Subjects.selectAll().where { Subjects.id eq id }
                .map { rowToSubject(it) }.singleOrNull()
        }

    override suspend fun getSubjectsByInfo(infoId: Int): List<Subject> =
        dbQuery {
            Subjects.selectAll().where { Subjects.infoId eq infoId }
                .map { rowToSubject(it) }.requireNoNulls()
        }

    override suspend fun addSubject(title: String, short: String, info: Int) =
        dbQuery {
            val id = Subjects.insert {
                it[this.title] = title
                it[this.shortTitle] = short
                it[this.infoId] = info
            }[Subjects.id]

            Subject(id.value, title, short, info)
        }


    override suspend fun getAllSchedules(): List<Schedule> =
        dbQuery {
            Schedules.selectAll().map { rowToSchedule(it) }.requireNoNulls()
        }

    override suspend fun addSchedule(title: String, info: Int) =
        dbQuery {
            val id = Schedules.insert {
                it[this.title] = title
                it[this.infoId] = info
            }[Schedules.id]
            Schedule(id = id.value, title = title, infoId = info, lastUpdate = Instant.now().toEpochMilli())
        }


    override suspend fun updateSchedule(schedule: Schedule) {
        dbQuery {
            Schedules.update({ Schedules.id eq schedule.id }) {
                it[this.title] = schedule.title
                it[this.infoId] = schedule.infoId
                it[this.lastUpdate] = Instant.now()
            }
        }
    }

    override suspend fun deleteSchedule(id: Int): Boolean =
        dbQuery {
            Schedules.deleteWhere { Schedules.id eq id } == 1
        }

    override suspend fun getScheduleById(id: Int): Schedule? =
        dbQuery {
            Schedules.selectAll().where { Schedules.id eq id }
                .map { rowToSchedule(it) }.singleOrNull()
        }

    override suspend fun getScheduleByInfo(infoId: Int): Schedule? =
        dbQuery {
            Schedules.selectAll().where { Schedules.infoId eq infoId }
                .map { rowToSchedule(it) }.singleOrNull()
        }

    override suspend fun getAllStudies(): List<Study> =
        dbQuery {
            Studies.join(Subjects, JoinType.INNER, Studies.subjectId, Subjects.id)
                .join(Teachers, JoinType.INNER, Studies.teacherId, Teachers.userId)
                .selectAll().map { rowToStudy(it) }.requireNoNulls()
        }

    override suspend fun addStudy(
        subject: Subject,
        day: DaysOfWeek,
        number: Int,
        type: TypeOfStudy,
        time: String,
        auditorium: String,
        teacher: Teacher,
        scheduleId: Int
    ): Study =
        dbQuery {
            val id = Studies.insert {
                it[this.subjectId] = subject.id
                it[this.day] = day
                it[this.number] = number
                it[this.type] = type
                it[this.time] = time
                it[this.auditorium] = auditorium
                it[this.teacherId] = teacher.userId
                it[this.scheduleId] = scheduleId
            }[Studies.id]

            Study(
                id = id.value,
                subject = subject,
                day = day,
                number = number,
                type = type,
                time = time,
                auditorium = auditorium,
                teacher = teacher,
                scheduleId = scheduleId
            )
        }

    override suspend fun updateStudy(study: Study) {
        dbQuery {
            Studies.update({ Studies.id.eq(study.id) }) {
                it[this.subjectId] = study.id
                it[this.day] = study.day
                it[this.number] = study.number
                it[this.type] = study.type
                it[this.time] = study.time
                it[this.auditorium] = study.auditorium
                it[this.teacherId] = study.teacher.userId
                it[this.scheduleId] = study.scheduleId
            }
        }
    }

    override suspend fun deleteStudy(id: Int): Boolean =
        dbQuery {
            Studies.deleteWhere { Studies.id eq id } == 1
        }

    override suspend fun getStudyById(id: Int): Study? =
        dbQuery {
            Studies.join(Subjects, JoinType.INNER, Studies.subjectId, Subjects.id)
                .join(Teachers, JoinType.INNER, Studies.teacherId, Teachers.userId)
                .selectAll().where { Studies.id eq id }.map { rowToStudy(it) }.singleOrNull()
        }

    override suspend fun getStudiesForScheduleId(scheduleId: Int): List<Study> =
        dbQuery {
            Studies.join(Subjects, JoinType.INNER, Studies.subjectId, Subjects.id)
                .join(Teachers, JoinType.INNER, Studies.teacherId, Teachers.userId)
                .selectAll().where { Studies.scheduleId eq scheduleId }
                .orderBy(Studies.day to SortOrder.ASC, Studies.number to SortOrder.ASC)
                .map { rowToStudy(it) }.requireNoNulls()
        }

    override suspend fun getStudiesForScheduleByDay(scheduleId: Int, day: DaysOfWeek): List<Study> =
        dbQuery {
            Studies.join(Subjects, JoinType.INNER, Studies.subjectId, Subjects.id)
                .join(Teachers, JoinType.INNER, Studies.teacherId, Teachers.userId)
                .selectAll().where { (Studies.scheduleId eq scheduleId) and (Studies.day eq day) }
                .map { rowToStudy(it) }.requireNoNulls()
        }

    override suspend fun getStudiesForTeacherByDay(teacherId: UUID, day: DaysOfWeek): List<Study> =
        dbQuery {
            Studies.join(Subjects, JoinType.INNER, Studies.subjectId, Subjects.id)
                .join(Teachers, JoinType.INNER, Studies.teacherId, Teachers.userId)
                .selectAll().where { Studies.teacherId eq teacherId }.map { rowToStudy(it) }.requireNoNulls()
        }

    override suspend fun addScheduleToStudent(scheduleId: Int, studentId: UUID, isMain: Boolean) {
        dbQuery {
            StudentsToSchedules.insert {
                it[this.scheduleId] = scheduleId
                it[this.studentId] = studentId
                it[this.isMain] = isMain
            }
        }
    }

    override suspend fun deleteScheduleFromStudent(scheduleId: Int, studentId: UUID): Boolean =
        dbQuery {
            StudentsToSchedules
                .deleteWhere { (StudentsToSchedules.scheduleId eq scheduleId) and (StudentsToSchedules.studentId eq studentId) } == 1
        }

    override suspend fun getSchedulesForStudent(studentId: UUID): List<Schedule> =
        dbQuery {
            StudentsToSchedules.join(Schedules, JoinType.INNER, StudentsToSchedules.scheduleId, Schedules.id)
                .join(Students, JoinType.INNER, StudentsToSchedules.studentId, Students.userId)
                .selectAll().where { StudentsToSchedules.studentId eq studentId }
                .map { rowToSchedule(it) }.requireNoNulls()
        }

    private fun rowToSubject(row: ResultRow?): Subject? {
        if (row == null) {
            return null
        }

        return Subject(
            id = row[Subjects.id].value,
            infoId = row[Subjects.infoId],
            shortTitle = row[Subjects.shortTitle],
            title = row[Subjects.title],
        )
    }

    private fun rowToSchedule(row: ResultRow?): Schedule? {
        if (row == null) {
            return null
        }

        return Schedule(
            id = row[Schedules.id].value,
            title = row[Schedules.title],
            lastUpdate = row[Schedules.lastUpdate].toEpochMilli(),
            infoId = row[Schedules.infoId],
        )
    }

    private fun rowToStudy(row: ResultRow?): Study? {
        if (row == null) {
            return null
        }

        return Study(
            id = row[Studies.id].value,
            subject = Subject(
                id = row[Subjects.id].value,
                title = row[Subjects.title],
                shortTitle = row[Subjects.shortTitle],
                infoId = row[Subjects.infoId]
            ),
            day = row[Studies.day],
            number = row[Studies.number],
            type = row[Studies.type],
            auditorium = row[Studies.auditorium],
            teacher = Teacher(
                userId = row[Teachers.userId].value,
                name = row[Teachers.name],
                academicTitle = row[Teachers.academicTitle],
                facultyId = row[Teachers.facultyId]
            ),
            time = row[Studies.time],
            scheduleId = row[Studies.scheduleId].value
        )
    }
}