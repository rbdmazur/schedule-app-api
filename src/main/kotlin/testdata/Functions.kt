package com.example.testdata

import com.example.repository.model.Info
import com.example.repository.model.Student
import com.example.repository.repositories.FacultyRepository
import com.example.repository.repositories.ScheduleRepository
import com.example.repository.repositories.UsersRepository
import com.example.utils.Constants.TEST_DATA_PATH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

val facultyRepository = FacultyRepository()
val usersRepository = UsersRepository()
val scheduleRepository = ScheduleRepository()

suspend fun addFaculties() {
    val file = File(TEST_DATA_PATH + "faculties.txt")
    val sc = withContext(Dispatchers.IO) {
        Scanner(file)
    }

    while (sc.hasNextLine()) {
        val line = sc.nextLine()
        val prop = line.split(" - ")

        println(facultyRepository.addFaculty(prop[0], prop[1]))
    }
}

suspend fun deleteFaculty(vararg id: Int) {
    val iterator = id.iterator()
    while (iterator.hasNext()) {
        println(facultyRepository.deleteFaculty(iterator.next()))
    }
}

suspend fun addUsers() {
    val file = File(TEST_DATA_PATH + "users.txt")
    val sc = withContext(Dispatchers.IO) {
        Scanner(file)
    }

    while (sc.hasNextLine()) {
        val line = sc.nextLine()
        val prop = line.split(" - ")

        val email = prop[0]
        val password = prop[1]

        usersRepository.addUser(email, password)
    }
}

suspend fun addInfo() {
    val file = File(TEST_DATA_PATH + "info.txt")
    val sc = withContext(Dispatchers.IO) {
        Scanner(file)
    }

    while (sc.hasNextLine()) {
        val line = sc.nextLine()
        val prop = line.split(" - ")
        val facultyName = prop[0]
        val spec = prop[1]
        val course = prop[2].toInt()
        val group = prop[3].toInt()

        val faculty = facultyRepository.getFacultyByName(facultyName)!!

        facultyRepository.addInfo(
            facultyId = faculty.id,
            specialization = spec,
            course = course,
            group = group
        )
    }

}

suspend fun addTeachers() {
    val file = File(TEST_DATA_PATH + "teachers.txt")
    val sc = withContext(Dispatchers.IO) {
        Scanner(file)
    }

    while (sc.hasNextLine()) {
        val line = sc.nextLine()
        val prop = line.split(" - ")

        val email = prop[0]
        val password = prop[1]
        val name = prop[2]
        val academ = prop[3]
        val facultyName = prop[4]

        val userId = usersRepository.addUser(email, password).id
        val faculty = facultyRepository.getFacultyByName(facultyName)!!

        usersRepository.addTeacher(userId, name, academ, faculty.id)
    }
}

suspend fun addStudents() {
    val file = File(TEST_DATA_PATH + "students.txt")
    val sc = withContext(Dispatchers.IO) {
        Scanner(file)
    }

    while (sc.hasNextLine()) {
        val line = sc.nextLine()
        println(line)
        val prop = line.split(" - ")
        val faculty = facultyRepository.getFacultyByName(prop[0])!!
        println("Faculty: $faculty")
        val groups = prop[1].toInt()
        for (i in 0 until groups) {
            val l = sc.nextLine()
            println(l)
            val prop1 = l.split(" - ")
            println(prop1.size)
            val course = prop1[0].toInt()
            val group = prop1[1].toInt()
            val students = prop1[2].toInt()
            println("group: $group, course: $course")

            val info = facultyRepository.getInfoByFacultyCourseGroup(
                facultyId = faculty.id,
                course = course,
                group = group
            )
            println("Info: $info")
            if (info == null) {
                break
            }

            for (i in 0 until students) {
                val str = sc.nextLine()
                val prop2 = str.split(" - ")
                val name = prop2[0]
                val email = prop2[1]
                val password = prop2[2]

                val user = usersRepository.findUserByEmail(email) ?: usersRepository.addUser(email, password)
                println("user: $user")

                usersRepository.addStudent(user.id, name, info)
            }
        }
    }
}

suspend fun getStudents(facultyId: Int, course: Int, group: Int) {
    println(usersRepository.getStudentsByCourse(facultyId, course))
}

suspend fun addSubjects() {
    val file = File(TEST_DATA_PATH + "subjects.txt")
    val sc = withContext(Dispatchers.IO) {
        Scanner(file)
    }

    while (sc.hasNextLine()) {
        val facultyName = sc.nextLine()
        val facultyId = facultyRepository.getFacultyByName(facultyName)!!.id
        val line = sc.nextLine()
        val prop = line.split(" - ")
        val course = prop[0].toInt()
        val group = prop[1].toInt()
        val count = prop[2].toInt()
        val info = facultyRepository.getInfoByFacultyCourseGroup(facultyId, course, group) ?: break

        for (i in 0 until count) {
            val str = sc.nextLine()
            val prop1 = str.split(" - ")
            val title = prop1[0]
            val short = prop1[1]

            scheduleRepository.addSubject(title, short, info.id)
        }
    }
}

suspend fun addScheduleToStudents(scheduleId: Int) {
    val students = usersRepository.getStudentsByGroup(1, 1, 1)
    for (student in students) {
        scheduleRepository.addScheduleToStudent(scheduleId, student.userId, true)
    }
}