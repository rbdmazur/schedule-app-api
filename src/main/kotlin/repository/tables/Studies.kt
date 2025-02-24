package com.example.repository.tables

import com.example.utils.DaysOfWeek
import com.example.utils.TypeOfStudy
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.postgresql.util.PGobject

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

object Studies : IntIdTable("studies") {
    val subjectId = reference("subject_id", Subjects.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val day = customEnumeration(
        "day",
        "DaysOfWeek",
        {value -> DaysOfWeek.valueOf(value as String) },
        { PGEnum("DaysOfWeek", it) }
    )
    val number = integer("number")
    val time = varchar("time", 12)
    val type = customEnumeration(
        "type",
        "TypesOfStudy",
        {value -> TypeOfStudy.valueOf(value as String) },
        { PGEnum("TypesOfStudy", it) }
    )
    val auditorium = varchar("auditorium", 32)
    val teacherId = reference("teacher_id", Teachers.userId, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable()
    val scheduleId = reference("schedule_id", Schedules.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

}