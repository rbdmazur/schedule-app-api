package com.example.repository.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Infos : Table("info") {
    val id = integer("id").autoIncrement()
    val facultyId = reference("faculty", Faculties.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val specialization = varchar("specialization", 128)
    val course = integer("course")
    val group = integer("group")

    override val primaryKey = PrimaryKey(id)

    init {
        index("info_index", isUnique = true, facultyId, specialization, course, group)
    }
}