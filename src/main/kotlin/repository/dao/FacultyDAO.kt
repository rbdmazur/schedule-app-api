package com.example.repository.dao

import com.example.repository.model.Faculty

interface FacultyDAO {
    suspend fun addFaculty(title: String, fullTitle: String): Faculty
    suspend fun deleteFaculty(facultyId: Int): Boolean
    suspend fun getAllFaculties(): List<Faculty>
    suspend fun getFaculty(facultyId: Int): Faculty?
    suspend fun getFacultyByName(name: String): Faculty?
}