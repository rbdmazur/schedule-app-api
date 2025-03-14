package com.example.routes

import com.example.routes.responses.ScheduleResponse
import com.example.routes.responses.StudiesResponse
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
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val schedules = scheduleService.getSchedulesForStudent(UUID.fromString(userId))
                val scheduleResponse = ScheduleResponse(schedules)

                call.respond(HttpStatusCode.OK, scheduleResponse)
            }

            get("/{scheduleId}") {
                val scheduleId = call.parameters["scheduleId"]
                if (scheduleId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val studies = scheduleService.getStudiesForScheduleId(scheduleId.toInt())
                val response = StudiesResponse(studies)
                call.respond(HttpStatusCode.OK, response)
            }
        }

        route("/all-schedules") {
            get {
                val schedules = scheduleService.getAllSchedules()

                if (schedules.isEmpty()) {
                    call.respond(HttpStatusCode.NoContent)
                    return@get
                }

                call.respond(HttpStatusCode.OK, schedules)
            }

            route("/{faculty}") {
                get {
                    val faculty = call.parameters["faculty"]
                    if (faculty == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    val schedules = scheduleService.getSchedulesForFaculty(faculty.toInt())
                    call.respond(HttpStatusCode.OK, schedules)
                }
                get("/{course}") {
                    val faculty = call.parameters["faculty"]
                    val course = call.parameters["course"]
                    if (course == null || faculty == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    val schedules = scheduleService.getSchedulesForCourse(faculty.toInt(), course.toInt())
                    call.respond(HttpStatusCode.OK, schedules)
                }
            }
        }
    }
}