package com.surya.fubotvtask.events

import java.time.LocalDateTime

sealed class Event{
    data class Header(val eventDate: LocalDateTime) : Event()
    data class Data(val meetingDetails: String) : Event()
}
