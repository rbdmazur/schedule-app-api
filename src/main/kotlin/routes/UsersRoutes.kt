package com.example.routes

import com.example.repository.repositories.UsersRepository
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Route.userRoutes(usersRepository: UsersRepository) {
    authenticate("auth-session") {

    }

    route("/users") {
        get {
            val users = usersRepository.getAllUsers()
            call.respond(users)
        }
    }

    route("/students") {
        get {
            val students = usersRepository.getAllStudents()
            call.respond(students)
        }
    }

    route("/teachers") {
        get {
            val teachers = usersRepository.getAllTeachers()
            call.respond(teachers)
        }
    }
}