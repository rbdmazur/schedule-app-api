package com.example.routes

import com.example.routes.requests.ScheduleRequest
import com.example.routes.requests.ScheduleRequestList
import com.example.routes.responses.ScheduleResponse
import com.example.routes.responses.ScheduleSimpleResponse
import com.example.routes.responses.StudiesResponse
import com.example.service.ScheduleService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
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

            get("/{userId}") {
                val userId = call.parameters["userId"]
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                }
                val facultyId = call.parameters["facultyId"]
                val course = call.parameters["course"]
                val group = call.parameters["group"]

                val schedules = scheduleService.getSchedulesByFilter(
                    userId = UUID.fromString(userId),
                    facultyId = facultyId?.toInt(),
                    course = course?.toInt(),
                    group = group?.toInt()
                )

                val response = ScheduleSimpleResponse(schedules)
                call.respond(HttpStatusCode.OK, response)
            }

            post("/{userId}") {
                val userId = call.parameters["userId"]
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                }

                val request = call.receive<ScheduleRequestList>()

                request.list.forEach {
                    scheduleService.addScheduleToStudent(
                        scheduleId = it.scheduleId.toInt(),
                        studentId = UUID.fromString(userId),
                        isMain = it.isMain
                    )
                }
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}