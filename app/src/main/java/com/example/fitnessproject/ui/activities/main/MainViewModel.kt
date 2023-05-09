package com.example.fitnessproject.ui.activities.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.fitnessproject.ui.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : BaseViewModel(application) {

    val dayLiveData = MutableLiveData<Int>()
    override fun onCreate() {
        super.onCreate()
        checkToShowDialogCongratulation()
    }

    private fun checkToShowDialogCongratulation() {
        val current = Calendar.getInstance()
        val last = sharePreference.getLastTime()
        last?.let {
            val diff = current.time.time - it.time
            val diffDay = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            if (diffDay == 1L) {
                val dayPractice = sharePreference.getDayPractice()
                if (dayPractice > 0) {
                    dayLiveData.value = dayPractice
                }
                sharePreference.saveDaysPractice(dayPractice + 1)
            } else if (diffDay > 1) {
                sharePreference.saveDaysPractice(0)
            }
            sharePreference.saveLastTime(current.time)
        } ?: sharePreference.saveLastTime(Calendar.getInstance().time)
    }
}