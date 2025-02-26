package com.example.routes

import com.example.auth.hashing.HashingService
import com.example.auth.token.TokenConfig
import com.example.auth.token.TokenService
import com.example.service.FacultyService
import com.example.service.ScheduleService
import com.example.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService,
    facultyService: FacultyService,
    scheduleService: ScheduleService,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(userService, hashingService, tokenService, tokenConfig)
        scheduleRoutes(scheduleService)
    }
}