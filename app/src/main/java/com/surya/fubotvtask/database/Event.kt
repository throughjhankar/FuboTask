package com.surya.fubotvtask.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Event(
    @PrimaryKey val uid: UUID,
    @ColumnInfo(name = "event_title") val eventTitle: String,
    @ColumnInfo(name = "event_start_date_time") val startDateTime: LocalDateTime,
    @ColumnInfo(name = "event_end_date_time") val endDateTime: LocalDateTime,
)
