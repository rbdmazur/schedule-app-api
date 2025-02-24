package com.example.repository.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object Schedules : IntIdTable("schedules") {
    val title = varchar("title", 128)
    val lastUpdate = timestamp("last_update").default(Instant.now())
    val infoId = reference("info_id", Infos.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}