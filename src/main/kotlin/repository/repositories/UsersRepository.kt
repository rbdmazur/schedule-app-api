package com.example.repository.repositories

import com.example.repository.dao.StudentDAO
import com.example.repository.dao.TeacherDAO
import com.example.repository.dao.UserDAO
import com.example.repository.dbQuery
import com.example.repository.model.*
import com.example.repository.tables.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class UsersRepository : UserDAO, TeacherDAO, StudentDAO {
    override suspend fun addUser(email: String, password: String): User =
        dbQuery {
            val id = Users.insert {
                it[this.email] = email
                it[this.password] = password
            }[Users.id].value

            User(id, email, password)
        }

    override suspend fun updateUser(user: User) {
        dbQuery {
            Users.update({ Users.id eq user.id }) {
                it[this.email] = user.email
                it[this.password] = user.password
            }
        }
    }

    override suspend fun deleteUser(user: User) =
        dbQuery {
            Users.deleteWhere { Users.id eq user.id } == 1
        }

    override suspend fun findUserById(userId: UUID): User? =
        dbQuery {
            Users.selectAll().where { Users.id eq userId }
                .map { rowToUser(it) }.singleOrNull()
        }

    override suspend fun findUserByEmail(email: String): User? =
        dbQuery {
            Users.selectAll().where { Users.email eq email }
                .map { rowToUser(it) }.singleOrNull()
        }

    override suspend fun getAllUsers(): List<User> =
        dbQuery {
            Users.selectAll().map { rowToUser(it) }.requireNoNulls()
        }

    override suspend fun getAllTeachers(): List<Teacher> =
        dbQuery {
            Teachers.selectAll().map { rowToTeacher(it) }.requireNoNulls()
        }

    override suspend fun addTeacher(id: UUID, name: String, academicTitle: String, facultyId: Int): Teacher? =
        dbQuery {
            val row = Users.selectAll().where { Users.id eq id }
            if (row.empty()) {
                null
            } else {
                Teachers.insert {
                    it[userId] = id
                    it[this.name] = name
                    it[this.academicTitle] = academicTitle
                    it[this.facultyId] = facultyId
                }
                Teacher(id, name, academicTitle, facultyId)
            }
        }

    override suspend fun deleteTeacher(id: UUID): Boolean =
        dbQuery {
            Teachers.deleteWhere { userId eq id } == 1
        }

    override suspend fun updateTeacher(teacher: Teacher) {
        dbQuery {
            Teachers.update({ Teachers.userId eq teacher.userId }) {
                it[name] = teacher.name
                it[academicTitle] = teacher.academicTitle
                it[facultyId] = teacher.facultyId
            }
        }
    }

    override suspend fun getTeacherById(id: UUID): Teacher? =
        dbQuery {
            Teachers.join(Users, JoinType.INNER, Teachers.userId, Users.id).selectAll()
                .where { Teachers.userId eq id }.map { rowToTeacher(it) }.singleOrNull()
        }

    override suspend fun getTeacherByEmail(email: String): Teacher? =
        dbQuery {
            Teachers.join(Users, JoinType.INNER, Teachers.userId, Users.id).selectAll()
                .where { Users.email eq email }.map { rowToTeacher(it) }.singleOrNull()
        }

    override suspend fun getTeachersByFaculty(facultyId: Int): List<Teacher> =
        dbQuery {
            Teachers.selectAll().where { Teachers.facultyId eq facultyId }
                .orderBy(Teachers.name to SortOrder.ASC)
                .map { rowToTeacher(it) }.requireNoNulls()
        }

    override suspend fun getAllStudents(): List<Student> =
        dbQuery {
            Students.join(Infos, JoinType.INNER, Students.infoId, Infos.id)
                .selectAll().map { rowToStudent(it) }.requireNoNulls()
        }

    override suspend fun updateStudent(student: Student) {
        dbQuery {
            Students.update ({ Students.userId eq student.userId }) {
                it[this.name] = student.name
                it[this.infoId] = student.info.id
            }
        }
    }

    override suspend fun deleteStudent(id: UUID): Boolean =
        dbQuery {
            Students.deleteWhere { userId eq id } == 1
        }

    override suspend fun addStudent(id: UUID, name: String, info: Info): Student? =
        dbQuery {
            val userRow = Users.selectAll().where {Users.id eq id}
            if (userRow.empty()) {
                null
            } else {
                Students.insert {
                    it[userId] = id
                    it[this.name] = name
                    it[infoId] = info.id
                }
                Student(id, name, info)
            }
        }

    override suspend fun getStudentById(id: UUID): Student? =
        dbQuery {
            Students.join(Infos, JoinType.INNER, Students.infoId, Infos.id)
                .selectAll().where { Students.userId eq id }.map { rowToStudent(it) }.singleOrNull()
        }

    override suspend fun getStudentByEmail(email: String): Student? =
        dbQuery {
            Students.join(Users, JoinType.INNER, Students.userId, Users.id)
                .join(Infos, JoinType.INNER, Students.infoId, Infos.id)
                .selectAll()
                .where { Users.email eq email }.map { rowToStudent(it) }.singleOrNull()
        }

    override suspend fun getStudentsByFaculty(facultyId: Int): List<Student> =
        dbQuery {
            Students.join(Infos, JoinType.INNER, Students.infoId, Infos.id)
                .selectAll().where { Infos.facultyId eq facultyId }
                .orderBy(Infos.course to SortOrder.DESC, Infos.group to SortOrder.DESC, Students.name to SortOrder.ASC)
                .map { rowToStudent(it) }.requireNoNulls()
        }

    override suspend fun getStudentsByCourse(facultyId: Int, course: Int): List<Student> =
        dbQuery {
            Students.join(Infos, JoinType.INNER, Students.infoId, Infos.id)
                .selectAll().where { (Infos.facultyId eq facultyId) and (Infos.course eq course) }
                .orderBy(Infos.group to SortOrder.DESC, Students.name to SortOrder.ASC)
                .map { rowToStudent(it) }
                .requireNoNulls()
        }


    override suspend fun getStudentsByGroup(facultyId: Int, course: Int, group: Int): List<Student> =
        dbQuery {
            Students.join(Infos, JoinType.INNER, Students.infoId, Infos.id)
                .selectAll()
                .where { (Infos.facultyId eq facultyId) and (Infos.course eq course) and (Infos.group eq group) }
                .orderBy(Students.name to SortOrder.ASC)
                .map { rowToStudent(it) }
                .requireNoNulls()
        }

    private fun rowToTeacher(row: ResultRow?): Teacher? {
        if (row == null) {
            return null
        }

        return Teacher(
            userId = row[Teachers.userId].value,
            name = row[Teachers.name],
            academicTitle = row[Teachers.academicTitle],
            facultyId = row[Teachers.facultyId],
        )
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }

        return User(
            id = row[Users.id].value,
            email = row[Users.email],
            password = row[Users.password]
        )
    }

    private fun rowToStudent(row: ResultRow?): Student? {
        if (row == null) {
            return null
        }
        return Student(
            userId = row[Students.userId].value,
            name = row[Students.name],
            info = Info(
                id = row[Students.infoId],
                facultyId = row[Infos.facultyId],
                specialization = row[Infos.specialization],
                course = row[Infos.course],
                group = row[Infos.group]
            )
        )
    }
}