package com.example.fitnessproject.ui.fragments.hoso

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.view.View
import com.example.fitnessproject.AlarmBroadCast
import com.example.fitnessproject.R
import com.example.fitnessproject.domain.model.Day
import com.example.fitnessproject.domain.model.Reminder
import com.example.fitnessproject.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_remider.*
import java.util.*

class RemiderActivity : BaseActivity<InformationViewModel>() {
    override fun getLayoutId() = R.layout.layout_remider

    override fun initScreen() {
        viewModel.getReminder()
        simpleTimePicker?.setIs24HourView(true)
        val alarmManager = getSystemService(Service.ALARM_SERVICE) as AlarmManager

        btnMonday?.setOnClickListener {
            addOrRemoveDay(Day(Reminder.ID_MONDAY, Calendar.MONDAY))
        }

        btnTuesDay?.setOnClickListener {
            addOrRemoveDay(Day(Reminder.ID_TUESDAY, Calendar.TUESDAY))
        }

        btnWednesday?.setOnClickListener {
            addOrRemoveDay(Day(Reminder.ID_WEDNESDAY, Calendar.WEDNESDAY))
        }
        btnThursday?.setOnClickListener {
            addOrRemoveDay(Day(Reminder.ID_THURSDAY, Calendar.THURSDAY))
        }

        btnFriday?.setOnClickListener {
            addOrRemoveDay(Day(Reminder.ID_FRIDAY, Calendar.FRIDAY))
        }

        btnSaturday?.setOnClickListener {
            addOrRemoveDay(Day(Reminder.ID_SATURDAY, Calendar.SATURDAY))
        }
        btnSunday?.setOnClickListener {
            addOrRemoveDay(Day(Reminder.ID_SUNDAY, Calendar.SUNDAY))
        }

        btnSave.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                viewModel.reminder?.hour = simpleTimePicker.hour
                viewModel.reminder?.minute = simpleTimePicker.minute
            } else {
                viewModel.reminder?.hour = simpleTimePicker.currentHour
                viewModel.reminder?.minute = simpleTimePicker.currentMinute
            }
            viewModel.saveReminder()
            alarm(alarmManager)
            finish()
        }

        title_reminder?.setOnClickListener {
            finish()
        }
    }

    override fun bindData() {
        super.bindData()
        viewModel.reminderLiveData.observe(this) {
            setTime(it)
        }
    }

    private fun addOrRemoveDay(day: Day) {
        val dayExisted = viewModel.reminder?.days?.firstOrNull { it.id == day.id }
        dayExisted?.let {
            if (viewModel.reminder?.days?.size == 1) return
            viewModel.reminder?.days?.remove(it)
        } ?: viewModel.reminder?.days?.add(day)
        viewModel.reminderLiveData.value = viewModel.reminder
    }

    private fun View.showSelection(isSelection: Boolean) {
        this.setBackgroundResource(if (isSelection) R.drawable.bg_weekdays else R.drawable.bg_weekdays_normal)
    }

    private fun setTime(reminder: Reminder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            simpleTimePicker.hour = reminder.hour
            simpleTimePicker.minute = reminder.minute
        } else {
            simpleTimePicker.currentHour = reminder.hour
            simpleTimePicker.currentMinute = reminder.minute
        }
        btnMonday?.showSelection(reminder.days.firstOrNull { it.id == Reminder.ID_MONDAY } != null)
        btnTuesDay?.showSelection(reminder.days.firstOrNull { it.id == Reminder.ID_TUESDAY } != null)
        btnWednesday?.showSelection(reminder.days.firstOrNull { it.id == Reminder.ID_WEDNESDAY } != null)
        btnThursday?.showSelection(reminder.days.firstOrNull { it.id == Reminder.ID_THURSDAY } != null)
        btnFriday?.showSelection(reminder.days.firstOrNull { it.id == Reminder.ID_FRIDAY } != null)
        btnSaturday?.showSelection(reminder.days.firstOrNull { it.id == Reminder.ID_SATURDAY } != null)
        btnSunday?.showSelection(reminder.days.firstOrNull { it.id == Reminder.ID_SUNDAY } != null)
    }

    private fun alarm(alarmManager: AlarmManager) {
        viewModel.reminder?.days?.forEach {
            val alarmIntent = Intent(this, AlarmBroadCast::class.java)
            val intent = PendingIntent.getBroadcast(
                this,
                it.id,
                alarmIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_CANCEL_CURRENT
            )
            alarmManager.cancel(intent)
        }

        viewModel.reminder?.let {
            it.days.forEach { item ->
                val alarmIntent = Intent(this, AlarmBroadCast::class.java)
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, it.hour)
                calendar.set(Calendar.MINUTE, it.minute)
                calendar.set(Calendar.DAY_OF_WEEK, item.day)
                val intent = PendingIntent.getBroadcast(
                    this,
                    item.id,
                    alarmIntent,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else 0
                )
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    intent
                )
            }

        }
    }

}