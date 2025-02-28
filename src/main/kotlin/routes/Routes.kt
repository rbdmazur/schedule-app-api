package com.example.routes

import com.example.auth.hashing.SHA256HashingService
import com.example.auth.token.JwtTokenService
import com.example.auth.token.TokenConfig
import com.example.di.DaggerRouteComponent
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    tokenConfig: TokenConfig
) {
    val routeComponent = DaggerRouteComponent.builder().build()

    val userService = routeComponent.getUserService()
    val facultyService = routeComponent.getFacultyService()
    val scheduleService = routeComponent.getScheduleService()

    val hashingService = SHA256HashingService()
    val tokenService = JwtTokenService()
    routing {
        signIn(tokenConfig, userService, hashingService, tokenService)
        scheduleRoutes(scheduleService)
        subjectRoutes(scheduleService, userService)
        infoRoutes(facultyService)
    }
}