package com.example.service

import com.example.repository.model.Info
import com.example.repository.model.Student
import com.example.repository.model.Teacher
import com.example.repository.model.User
import com.example.repository.repositories.UsersRepository
import java.util.UUID

class UserService(
    private val usersRepository: UsersRepository
) {
    //User
    suspend fun addUser(email: String, password: String): User =
        usersRepository.addUser(email, password)

    suspend fun updateUser(user: User) {
        usersRepository.updateUser(user)
    }

    suspend fun deleteUser(user: User): Boolean =
        usersRepository.deleteUser(user)

    suspend fun findUserById(userId: UUID): User? =
        usersRepository.findUserById(userId)

    suspend fun findUserByEmail(email: String): User? =
        usersRepository.findUserByEmail(email)

    suspend fun getAllUsers(): List<User> =
        usersRepository.getAllUsers()

    //Teacher
    suspend fun getAllTeachers(): List<Teacher> =
        usersRepository.getAllTeachers()

    suspend fun addTeacher(
        id: UUID,
        name: String,
        academicTitle: String,
        facultyId: Int
    ): Teacher? =
        usersRepository.addTeacher(id, name, academicTitle, facultyId)

    suspend fun deleteTeacher(id: UUID): Boolean =
        usersRepository.deleteTeacher(id)

    suspend fun updateTeacher(teacher: Teacher) {
        usersRepository.updateTeacher(teacher)
    }

    suspend fun getTeacherById(id: UUID): Teacher? =
        usersRepository.getTeacherById(id)

    suspend fun getTeacherByEmail(email: String): Teacher? =
        usersRepository.getTeacherByEmail(email)

    suspend fun getTeachersByFaculty(facultyId: Int): List<Teacher> =
        usersRepository.getTeachersByFaculty(facultyId)

    //Students
    suspend fun getAllStudents(): List<Student> =
        usersRepository.getAllStudents()

    suspend fun updateStudent(student: Student) {
        usersRepository.updateStudent(student)
    }

    suspend fun deleteStudent(id: UUID): Boolean =
        usersRepository.deleteStudent(id)

    suspend fun addStudent(
        id: UUID,
        name: String,
        info: Info
    ): Student? =
        usersRepository.addStudent(id, name, info)

    suspend fun getStudentById(id: UUID): Student? =
        usersRepository.getStudentById(id)

    suspend fun getStudentByEmail(email: String): Student? =
        usersRepository.getStudentByEmail(email)

    suspend fun getStudentsByFaculty(facultyId: Int): List<Student> =
        usersRepository.getStudentsByFaculty(facultyId)

    suspend fun getStudentsByCourse(facultyId: Int, course: Int): List<Student> =
        usersRepository.getStudentsByCourse(facultyId, course)

    suspend fun getStudentsByGroup(
        facultyId: Int,
        course: Int,
        group: Int
    ) : List<Student> =
        usersRepository.getStudentsByGroup(facultyId, course, group)
}