package com.example.repository.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Subjects : IntIdTable("subjects") {
    val title = varchar("title", 255)
    val shortTitle = varchar("short_title", 32).nullable()
    val infoId = reference("info_id", Infos.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    init {
        index(isUnique = true, title, infoId)
    }
}