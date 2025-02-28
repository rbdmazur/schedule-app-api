package com.example.routes

import com.example.repository.tables.StudentsToSchedules.studentId
import com.example.service.ScheduleService
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import java.util.*

fun Route.subjectRoutes(scheduleService: ScheduleService, userService: UserService) {
    authenticate {
        route("/subjects") {
            get("/all") {
                val subjects = scheduleService.getAllSubjects()
                if (subjects.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(HttpStatusCode.OK, subjects)
            }

            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val infoId = userService.getStudentById(UUID.fromString(userId))?.info?.id
                if (infoId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val subjects = scheduleService.getSubjectsByInfo(infoId)
                if (subjects.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(HttpStatusCode.OK, subjects)
            }

            get("/{subject}") {
                val subjectId = call.parameters["subject"]
                if (subjectId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val subj = scheduleService.getSubjectById(subjectId.toInt())
                if (subj == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(HttpStatusCode.OK, subj)
            }
        }
    }
}