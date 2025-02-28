package com.example.repository.repositories

import com.example.repository.dao.FacultyDAO
import com.example.repository.dao.InfoDAO
import com.example.repository.dbQuery
import com.example.repository.model.Faculty
import com.example.repository.model.Info
import com.example.repository.tables.Faculties
import com.example.repository.tables.Infos
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class FacultyRepository : FacultyDAO, InfoDAO {
    override suspend fun addFaculty(title: String, fullTitle: String) =
        dbQuery {
            val id = Faculties.insert {
                it[this.title] = title
                it[this.fullTitle] = fullTitle
            }[Faculties.id]

            Faculty(id, title, fullTitle)
        }

    override suspend fun deleteFaculty(facultyId: Int): Boolean =
         dbQuery {
            Faculties.deleteWhere { id eq facultyId  } == 1
        }


    override suspend fun getAllFaculties(): List<Faculty> =
        dbQuery {
            Faculties.selectAll().map { rowToFaculty(it) }.requireNoNulls()
        }


    override suspend fun getFaculty(facultyId: Int): Faculty? =
        dbQuery {
            Faculties.selectAll().where { Faculties.id eq facultyId }.map { rowToFaculty(it) }.singleOrNull()
        }

    override suspend fun getFacultyByName(name: String): Faculty? =
        dbQuery {
            Faculties.selectAll().where { Faculties.title eq name }.map { rowToFaculty(it) }.singleOrNull()
        }

    override suspend fun addInfo(facultyId: Int, specialization: String, course: Int, group: Int) =
        dbQuery {
            val id = Infos.insert {
                it[this.facultyId] = facultyId
                it[this.specialization] = specialization
                it[this.course] = course
                it[this.group] = group
            }[Infos.id]

            Info(id, facultyId, specialization, course, group)
        }


    override suspend fun updateInfo(info: Info) {
        dbQuery {
            Infos.update({ Infos.id eq info.id }) {
                it[facultyId] = info.facultyId
                it[specialization] = info.specialization
                it[course] = info.course
                it[group] = info.group
            }
        }
    }

    override suspend fun getInfoById(infoId: Int): Info? =
        dbQuery {
            Infos.selectAll().where { Infos.id eq infoId }.map { rowToInfo(it) }.singleOrNull()
        }

    override suspend fun deleteInfo(infoId: Int): Boolean =
        dbQuery {
            Infos.deleteWhere { id eq infoId } == 1
        }

    override suspend fun getAllInfo(): List<Info> =
        dbQuery {
            Infos.selectAll().map { rowToInfo(it) }.requireNoNulls()
        }

    override suspend fun getInfoByFacultyCourseGroup(facultyId: Int, course: Int, group: Int): Info? =
        dbQuery {
            Infos.selectAll()
                .where { (Infos.facultyId eq facultyId) and (Infos.course eq course) and (Infos.group eq group) }
                .map { rowToInfo(it) }
                .singleOrNull()
        }

    override suspend fun getInfosForFaculty(facultyId: Int): List<Info> =
        dbQuery {
            Infos.selectAll()
                .where { Infos.facultyId eq facultyId }
                .map { rowToInfo(it) }
                .requireNoNulls()
        }

    override suspend fun getInfosForCourse(facultyId: Int, course: Int): List<Info> =
        dbQuery {
            Infos.selectAll()
                .where { (Infos.facultyId eq facultyId) and (Infos.course eq course) }
                .map { rowToInfo(it) }
                .requireNoNulls()
        }

    private fun rowToFaculty(row: ResultRow?): Faculty? {
        if (row == null) {
            return null
        }

        return Faculty(
            id = row[Faculties.id],
            title = row[Faculties.title],
            fullTitle = row[Faculties.fullTitle]
        )
    }

    private fun rowToInfo(row: ResultRow?): Info? {
        if (row == null) {
            return null
        }

        return Info(
            id = row[Infos.id],
            facultyId = row[Infos.facultyId],
            specialization = row[Infos.specialization],
            course = row[Infos.course],
            group = row[Infos.group]
        )
    }
}