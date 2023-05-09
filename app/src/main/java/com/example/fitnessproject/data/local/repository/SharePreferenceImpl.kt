package com.example.fitnessproject.data.local.repository

import android.content.Context
import com.example.fitnessproject.FitnessApplication
import com.example.fitnessproject.domain.model.Gender
import com.example.fitnessproject.domain.model.Reminder
import java.text.SimpleDateFormat
import java.util.*

class SharePreferenceImpl(application: FitnessApplication) : SharePreference {
    private val sharePreference =
        application.getSharedPreferences(SharePreference.KEY_PREFER, Context.MODE_PRIVATE)

    companion object {

        @Volatile
        private var INSTANCE: SharePreferenceImpl? = null

        fun getSharePreference(application: FitnessApplication): SharePreferenceImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = SharePreferenceImpl(application)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun saveSetUpFirstTime(isSetup: Boolean) {
        sharePreference.edit().putBoolean(SharePreference.KEY_SETUP_FIRST_TIME, isSetup).apply()
    }

    override fun isSetupFirstTime() =
        sharePreference.getBoolean(SharePreference.KEY_SETUP_FIRST_TIME, false)

    override fun saveUserId(userId: Int) {
        sharePreference.edit().putInt(SharePreference.KEY_ID_USER, userId).apply()
    }

    override fun getUserId() = sharePreference.getInt(SharePreference.KEY_ID_USER, -1)

    override fun saveGender(gender: Int) {
        sharePreference.edit().putInt(SharePreference.KEY_GENDER, gender).apply()
    }

    override fun getGender() =
        Gender.valueOf(sharePreference.getInt(SharePreference.KEY_GENDER, -1))

    override fun saveReminder(reminder: Reminder) {
        sharePreference.edit().putString(SharePreference.KEY_REMINDER, reminder.toJson()).commit()
    }

    override fun getReminder(): Reminder? {
        val json = sharePreference.getString(SharePreference.KEY_REMINDER, "")
        return json?.let {
            if (json.isEmpty()) return null
            Reminder.fromJson(it)
        }
    }

    override fun saveLastTime(date: Date) {
        val format1 = SimpleDateFormat("dd/MM/yyyy")
        val dateString = format1.format(date)
        sharePreference.edit().putString(SharePreference.KEY_LAST_TIME, dateString).apply()
    }

    override fun getLastTime(): Date? {
        val lastTime = sharePreference.getString(SharePreference.KEY_LAST_TIME, "") ?: ""
        if (lastTime.isEmpty()) return null
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.parse(lastTime)
    }

    override fun saveDaysPractice(date: Int) {
        sharePreference.edit().putInt(SharePreference.KEY_DAYS, date).apply()
    }

    override fun getDayPractice() = sharePreference.getInt(SharePreference.KEY_DAYS, 0)
}