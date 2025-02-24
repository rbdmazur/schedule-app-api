package com.example.routes

import com.example.repository.repositories.ScheduleRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import java.util.*
import io.ktor.server.response.*

fun Route.schedulesRoutes(scheduleRepository: ScheduleRepository) {
    authenticate("auth-session") {

    }

    route("schedules") {
        route("/{userId}") {
            get {
                val userId = call.parameters["userId"]
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val schedules = scheduleRepository.getSchedulesForStudent(UUID.fromString(userId))
                if (schedules.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(schedules)
            }

            get("/{scheduleId}") {
                val scheduleId = call.parameters["scheduleId"]
                if (scheduleId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val studies = scheduleRepository.getStudiesForScheduleId(scheduleId.toInt())
                if (studies.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(studies)
            }
        }
    }
}