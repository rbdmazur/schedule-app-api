package com.example.repository.dao

import com.example.repository.model.Info
import com.example.repository.model.Student
import java.util.*

interface StudentDAO {
    suspend fun getAllStudents(): List<Student>
    suspend fun updateStudent(student: Student)
    suspend fun deleteStudent(id: UUID): Boolean
    suspend fun addStudent(id: UUID, name: String, info: Info): Student?
    suspend fun getStudentById(id: UUID): Student?
    suspend fun getStudentByEmail(email: String): Student?
    suspend fun getStudentsByFaculty(facultyId: Int): List<Student>
    suspend fun getStudentsByCourse(facultyId: Int, course: Int): List<Student>
    suspend fun getStudentsByGroup(facultyId: Int, course: Int, group: Int): List<Student>
}