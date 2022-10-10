package com.surya.fubotvtask.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Event::class], version = 1)
@TypeConverters(LocalDateTimeTypeConverter::class, UUIDTypeConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
