package com.example.repository.tables

import com.example.repository.tables.Students.check
import com.example.repository.tables.Students.uniqueIndex
import org.jetbrains.exposed.dao.id.UUIDTable

object Users : UUIDTable("users") {
    val email = varchar("email", 32).uniqueIndex()
    val password = varchar("password", 32)
}