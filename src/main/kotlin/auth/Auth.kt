package com.example.auth

import com.example.repository.repositories.UsersRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*


fun Application.configureAuth(usersRepository: UsersRepository) {
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60
        }
    }
    install(Authentication) {
        form("auth-form") {
            validate { credentials ->
                val user = usersRepository.findUserByEmail(credentials.name)
                if (user != null && user.password == credentials.password) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

        session<UserSession>("auth-session") {
            validate { session ->
                application.log.info("Validating session ${session.userId}")
                val user = usersRepository.findUserById(session.userId)
                if (user != null) {
                    session
                } else {
                    null
                }
            }

            challenge {
                call.respondRedirect("/login")
            }
        }
    }
}