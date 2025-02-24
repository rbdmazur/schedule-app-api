package com.example.repository.dao

import com.example.repository.model.Subject

interface SubjectDAO {
    suspend fun getAllSubjects(): List<Subject>
    suspend fun addSubject(title: String, short: String, info: Int) : Subject
    suspend fun updateSubject(subject: Subject)
    suspend fun deleteSubject(id: Int): Boolean

    suspend fun getSubjectById(id: Int): Subject?
    suspend fun getSubjectsByInfo(infoId: Int): List<Subject>
}