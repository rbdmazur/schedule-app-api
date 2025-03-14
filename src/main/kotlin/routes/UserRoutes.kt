package com.example.routes

import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import java.util.*

fun Route.userRoutes(userService: UserService) {
    authenticate {
        route("/user") {
            get {
                val userId = call.request.queryParameters["id"]
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val user = userService.getStudentById(UUID.fromString(userId))
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(HttpStatusCode.OK, user)
            }
        }
    }
}