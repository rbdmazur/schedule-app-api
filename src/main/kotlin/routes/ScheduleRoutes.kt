package com.example.routes

import com.example.service.ScheduleService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import java.util.*


fun Route.scheduleRoutes(scheduleService: ScheduleService) {
    authenticate {
        route("/schedules") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()
                println("User ID is $userId")
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val schedules = scheduleService.getSchedulesForStudent(UUID.fromString(userId))

                call.respond(HttpStatusCode.OK, schedules)
            }
        }
    }
}