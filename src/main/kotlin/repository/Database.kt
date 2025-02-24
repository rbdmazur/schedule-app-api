package com.example.repository

import com.example.repository.tables.*
import com.example.testdata.*
import com.example.utils.Constants.DB_PASSWORD
import com.example.utils.Constants.DB_URL
import com.example.utils.Constants.DB_USER
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {

    Database.connect(
        url = DB_URL,
        user = DB_USER,
        password = DB_PASSWORD
    )

    transaction {
        SchemaUtils.create(Users)
        SchemaUtils.create(Faculties)
    }

    transaction {
        SchemaUtils.create(Teachers)
        SchemaUtils.create(Infos)
    }

    transaction {
        SchemaUtils.create(Schedules)
        SchemaUtils.create(Subjects)
        SchemaUtils.create(Students)
    }

    transaction {
        exec("CREATE TYPE DaysOfWeek AS ENUM ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY');")
        exec("CREATE TYPE TypesOfStudy AS ENUM ('LECTURE', 'PRACTISE');")
    }

    transaction {
        SchemaUtils.create(Studies)
        SchemaUtils.create(StudentsToSchedules)
    }
    launch {
    }
}

suspend fun <T> dbQuery(block: () -> T): T =
    withContext(Dispatchers.IO) {
        transaction { block() }
    }
