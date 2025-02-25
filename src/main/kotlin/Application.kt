package com.example

import com.example.auth.configureAuth
import com.example.repository.configureDatabase
import com.example.repository.repositories.ScheduleRepository
import com.example.repository.repositories.UsersRepository
import com.example.routes.*
import com.example.testdata.configureTest
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val usersRepository = UsersRepository()
    val scheduleRepository = ScheduleRepository()
    install(ContentNegotiation) {
        json()
    }
    configureDatabase()
    configureAuth(usersRepository)

    routing {
        authRoutes()
        schedulesRoutes(scheduleRepository)
        subjectRoutes(scheduleRepository, usersRepository)
        userRoutes(usersRepository)
    }
}
