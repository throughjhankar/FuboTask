package com.surya.fubotvtask.database

import androidx.room.TypeConverter
import java.util.UUID

class UUIDTypeConvertor {
    @TypeConverter
    fun toDate(uid: String): UUID {
        return UUID.fromString(uid)
    }

    @TypeConverter
    fun fromDate(uid: UUID): String {
        return uid.toString()
    }
}
