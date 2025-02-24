package com.example.repository.tables

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Students : CompositeIdTable("students") {
    val userId = reference("user_id", Users.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val name = varchar("student_name", 128)
    val infoId = reference("info_id", Infos.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId)
}