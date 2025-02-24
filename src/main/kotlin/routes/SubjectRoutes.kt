package com.example.routes

import com.example.repository.repositories.ScheduleRepository
import com.example.repository.repositories.UsersRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import java.util.*

fun Route.subjectRoutes(scheduleRepository: ScheduleRepository, usersRepository: UsersRepository) {
    authenticate("auth-session") {

    }
    route("/subject") {
        route("/{userId}") {
            get {
                val userId = call.parameters["userId"]
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val student = usersRepository.getStudentById(UUID.fromString(userId))
                if (student == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                val infoId = student.info.id

                val subjects = scheduleRepository.getSubjectsByInfo(infoId)

                if (subjects.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(subjects)
            }
        }
    }
}