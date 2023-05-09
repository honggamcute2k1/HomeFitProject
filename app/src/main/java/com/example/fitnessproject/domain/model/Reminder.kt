package com.example.fitnessproject.domain.model

import com.google.gson.Gson

data class Reminder(
    var hour: Int,
    var minute: Int,
    var days: MutableList<Day>
) {

    companion object {
        const val ID_MONDAY = 1
        const val ID_TUESDAY = 2
        const val ID_WEDNESDAY = 3
        const val ID_THURSDAY = 4
        const val ID_FRIDAY = 5
        const val ID_SATURDAY = 6
        const val ID_SUNDAY = 7
        fun fromJson(json: String): Reminder {
            return Gson().fromJson(json, Reminder::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}

data class Day(val id: Int, val day: Int)