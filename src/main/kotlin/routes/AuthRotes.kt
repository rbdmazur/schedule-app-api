package com.example.routes

import com.example.auth.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

fun Route.authRoutes() {
    get("/test") {
        val userId = call.sessions.get<UserSession>()?.userId
        if (userId != null) {
            call.respondText { "Logged in as $userId" }
        }
    }
    authenticate("auth-form") {

    }
    post("/login") {
        val userId = call.principal<UserSession>()
        if (userId != null) {
            call.sessions.set(UserSession(userId.userId))
        }
        application.log.info("Logged in as $userId")
        call.respondRedirect("/test")
    }
    get("/login") {
        call.respondHtml {
            body {
                form(action = "/login", encType = FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
                    p {
                        +"Username:"
                        textInput(name = "username")
                    }
                    p {
                        +"Password:"
                        passwordInput(name = "password")
                    }
                    p {
                        submitInput() { value = "Login" }
                    }
                }
            }
        }
    }
}
