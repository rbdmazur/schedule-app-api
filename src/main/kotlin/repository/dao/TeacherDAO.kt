package com.example.repository.dao

import com.example.repository.model.Faculty
import com.example.repository.model.Teacher
import java.util.UUID

interface TeacherDAO {
    suspend fun getAllTeachers(): List<Teacher>
    suspend fun addTeacher(id: UUID, name: String, academicTitle: String, facultyId: Int): Teacher?
    suspend fun deleteTeacher(id: UUID): Boolean
    suspend fun updateTeacher(teacher: Teacher)
    suspend fun getTeacherById(id: UUID): Teacher?
    suspend fun getTeacherByEmail(email: String): Teacher?
    suspend fun getTeachersByFaculty(facultyId: Int): List<Teacher>
}