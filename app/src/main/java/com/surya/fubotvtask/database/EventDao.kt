package com.surya.fubotvtask.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun getAllEvents(): List<Event>

    @Insert
    fun insertEvent(vararg event: Event)
}
