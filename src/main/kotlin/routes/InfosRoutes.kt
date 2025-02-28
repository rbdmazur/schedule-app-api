package com.example.routes

import com.example.service.FacultyService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Route.infoRoutes(facultyService: FacultyService) {
    authenticate {
        route("/faculties") {
            get {
                val faculties = facultyService.getAllFaculties()
                if (faculties.isEmpty()) {
                    call.respond(HttpStatusCode.NoContent)
                    return@get
                }
                call.respond(HttpStatusCode.OK, faculties)
            }

            get("/{facultyId}") {
                val facultyId = call.parameters["facultyId"]
                if (facultyId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val infos = facultyService.getInfosForFaculty(facultyId.toInt())
                if (infos.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(HttpStatusCode.OK, infos)
            }
        }
    }
}