package com.example

import com.example.auth.configureSecurity
import com.example.auth.hashing.SHA256HashingService
import com.example.auth.token.JwtTokenService
import com.example.auth.token.TokenConfig
import com.example.repository.configureDatabase
import com.example.repository.repositories.FacultyRepository
import com.example.repository.repositories.ScheduleRepository
import com.example.repository.repositories.UsersRepository
import com.example.routes.configureRouting
import com.example.serialization.configureSerialization
import com.example.service.FacultyService
import com.example.service.ScheduleService
import com.example.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 15L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    configureSerialization()
    configureDatabase()
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
}