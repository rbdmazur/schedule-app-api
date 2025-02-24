package com.example.repository.tables

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Teachers : CompositeIdTable("teachers") {
    val userId = reference("user_id", Users.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val name = varchar("teacher_name", 128)
    val academicTitle = varchar("academic_title", 32).nullable()
    val facultyId = reference("faculty", Faculties.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId)
}