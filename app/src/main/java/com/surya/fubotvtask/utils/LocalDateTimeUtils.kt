package com.surya.fubotvtask.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.getTime(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}

fun getFormattedTime(hourOfDay: Int, minute: Int): String {
    return when {
        hourOfDay == 0 -> {
            if (minute < 10) {
                "${hourOfDay + 12}:0${minute} am"
            } else {
                "${hourOfDay + 12}:${minute} am"
            }
        }
        hourOfDay > 12 -> {
            if (minute < 10) {
                "${hourOfDay - 12}:0${minute} pm"
            } else {
                "${hourOfDay - 12}:${minute} pm"
            }
        }
        hourOfDay == 12 -> {
            if (minute < 10) {
                "${hourOfDay}:0${minute} pm"
            } else {
                "${hourOfDay}:${minute} pm"
            }
        }
        else -> {
            if (minute < 10) {
                "${hourOfDay}:${minute} am"
            } else {
                "${hourOfDay}:${minute} am"
            }
        }
    }
}
