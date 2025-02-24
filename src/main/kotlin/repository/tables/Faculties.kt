package com.example.repository.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Faculties : Table("faculties") {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 32)
    val fullTitle = varchar("full_title", 255)

    override val primaryKey = PrimaryKey(id)
}