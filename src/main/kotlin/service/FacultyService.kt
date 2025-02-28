package com.example.service

import com.example.repository.model.Faculty
import com.example.repository.model.Info
import com.example.repository.repositories.FacultyRepository
import javax.inject.Inject

class FacultyService @Inject constructor(
    private val facultyRepository: FacultyRepository
) {
    //Faculty
    suspend fun addFaculty(title: String, fullTitle: String): Faculty =
        facultyRepository.addFaculty(title, fullTitle)

    suspend fun deleteFaculty(facultyId: Int): Boolean =
        facultyRepository.deleteFaculty(facultyId)

    suspend fun getAllFaculties(): List<Faculty> =
        facultyRepository.getAllFaculties()

    suspend fun getFaculty(facultyId: Int): Faculty? =
        facultyRepository.getFaculty(facultyId)

    suspend fun getFacultyByName(name: String): Faculty? =
        facultyRepository.getFacultyByName(name)

    //Info
    suspend fun addInfo(
        facultyId: Int,
        specialization: String,
        course: Int,
        group: Int
    ) : Info =
        facultyRepository.addInfo(facultyId, specialization, course, group)

    suspend fun updateInfo(info: Info) {
        facultyRepository.updateInfo(info)
    }

    suspend fun getInfoById(infoId: Int): Info? =
        facultyRepository.getInfoById(infoId)

    suspend fun deleteInfo(infoId: Int): Boolean =
        facultyRepository.deleteInfo(infoId)

    suspend fun getAllInfo(): List<Info> =
        facultyRepository.getAllInfo()

    suspend fun getInfoByFacultyCourseGroup(
        facultyId: Int,
        course: Int,
        group: Int
    ) : Info? =
        facultyRepository.getInfoByFacultyCourseGroup(facultyId, course, group)

    suspend fun getInfosForFaculty(facultyId: Int): List<Info> =
        facultyRepository.getInfosForFaculty(facultyId)

    suspend fun getInfosForCourse(facultyId: Int, course: Int): List<Info> =
        facultyRepository.getInfosForCourse(facultyId, course)
}