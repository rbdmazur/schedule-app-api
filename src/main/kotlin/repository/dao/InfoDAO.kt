package com.example.repository.dao

import com.example.repository.model.Faculty
import com.example.repository.model.Info

interface InfoDAO {
    suspend fun addInfo(facultyId: Int, specialization: String, course: Int, group: Int): Info
    suspend fun updateInfo(info: Info)
    suspend fun getInfoById(infoId: Int): Info?
    suspend fun getInfoByFacultyCourseGroup(facultyId: Int, course: Int, group: Int): Info?
    suspend fun deleteInfo(infoId: Int): Boolean
    suspend fun getAllInfo(): List<Info>
}