package com.example.fitnessproject.data.local.repository

import com.example.fitnessproject.domain.model.Gender
import com.example.fitnessproject.domain.model.Reminder
import java.util.*

interface SharePreference {
    companion object {
        const val KEY_PREFER = "KEY_PREFER"
        const val KEY_SETUP_FIRST_TIME = "KEY_SETUP_FIRST_TIME"

        const val KEY_ID_USER = "KEY_ID_USER"
        const val KEY_GENDER = "KEY_GENDER"
        const val KEY_REMINDER = "KEY_REMINDER"
        const val KEY_LAST_TIME = "KEY_LAST_TIME"
        const val KEY_DAYS = "KEY_DAYS"
    }

    fun saveSetUpFirstTime(isSetup: Boolean)
    fun isSetupFirstTime(): Boolean

    fun saveUserId(userId: Int)
    fun getUserId(): Int

    fun saveGender(gender: Int)
    fun getGender(): Gender

    fun saveReminder(reminder: Reminder)
    fun getReminder(): Reminder?

    fun saveLastTime(date: Date)
    fun getLastTime(): Date?

    fun saveDaysPractice(date: Int)
    fun getDayPractice(): Int
}