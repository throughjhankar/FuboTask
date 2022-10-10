package com.surya.fubotvtask.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class LocalDateTimeTypeConverter {
    @TypeConverter
    fun toDate(dateLong: Long): LocalDateTime {
        return dateLong.let { LocalDateTime.ofInstant(
            Instant.ofEpochMilli(dateLong),
            ZoneId.systemDefault()) }
    }

    @TypeConverter
    fun fromDate(date: LocalDateTime): Long {
        return date.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli()
    }
}
