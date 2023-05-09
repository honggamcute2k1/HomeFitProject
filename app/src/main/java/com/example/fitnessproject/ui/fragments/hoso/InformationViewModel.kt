package com.example.fitnessproject.ui.fragments.hoso

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.fitnessproject.FitnessApplication
import com.example.fitnessproject.data.local.entity.User
import com.example.fitnessproject.domain.model.Day
import com.example.fitnessproject.domain.model.Reminder
import com.example.fitnessproject.domain.model.UserInformationModel
import com.example.fitnessproject.domain.usecase.user.UserUseCase
import com.example.fitnessproject.domain.usecase.user.UserUseCaseImpl
import com.example.fitnessproject.ui.BaseViewModel
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field.FIELD_HEIGHT
import com.google.android.gms.fitness.data.Field.FIELD_WEIGHT
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

open class InformationViewModel(application: Application) : BaseViewModel(application) {
    val userUseCase: UserUseCase =
        UserUseCaseImpl((application as FitnessApplication).userRepository)

    val userInfoLiveData = MutableLiveData<User>()
    val reminderLiveData = MutableLiveData<Reminder>()
    val syncGoogleFitLiveData = MutableLiveData<Boolean>()
    var syncGoogleFitData = false
    var reminder: Reminder? = null

    override fun onCreate() {
        super.onCreate()
    }

    fun getUserInformation() {
        showLoading(isShowLoading = true)
        myScope.launch {
            val user = withContext(Dispatchers.IO) {
                userUseCase.getAllUser().first()
            }
            userInfoLiveData.value = user
            showLoading(isShowLoading = false)
        }
    }

    fun getReminder() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        reminder =
            sharePreference.getReminder() ?: Reminder(
                hour = hour,
                minute = minute,
                days = mutableListOf<Day>().apply {
                    add(Day(Reminder.ID_MONDAY, Calendar.MONDAY))
                    add(Day(Reminder.ID_TUESDAY, Calendar.TUESDAY))
                    add(Day(Reminder.ID_WEDNESDAY, Calendar.WEDNESDAY))
                    add(Day(Reminder.ID_THURSDAY, Calendar.THURSDAY))
                    add(Day(Reminder.ID_FRIDAY, Calendar.FRIDAY))
                    add(Day(Reminder.ID_SATURDAY, Calendar.SATURDAY))
                    add(Day(Reminder.ID_SUNDAY, Calendar.SUNDAY))
                }
            )
        reminderLiveData.value = reminder
    }

    fun saveReminder() {
        reminder?.let { sharePreference.saveReminder(it) }
    }

    fun syncGoogleFit(response: Task<DataReadResponse>) {
        showLoading(isShowLoading = true)
        myScope.launch {
            delay(1500)
            withContext(Dispatchers.IO) {
                val readDataResponse = Tasks.await(response)
                val weightDataSet = readDataResponse.getDataSet(DataType.TYPE_WEIGHT)
                val heightDataSet = readDataResponse.getDataSet(DataType.TYPE_HEIGHT)
                val user = withContext(Dispatchers.IO) {
                    userUseCase.getAllUser().first()
                }
                val currentWeight = weightDataSet.dataPoints.lastOrNull()
                currentWeight?.let {
                    val weight = it.getValue(FIELD_WEIGHT)
                    user.weight = weight.asFloat().toDouble()
                }
                heightDataSet.dataPoints.firstOrNull()?.let { data ->
                    val height = data.getValue(FIELD_HEIGHT)
                    user.height = height.asFloat().toDouble() * 100
                }
                userUseCase.updateUser(user)
                val userId = user.userId
                val calendar = Calendar.getInstance()
                weightDataSet.dataPoints.forEach { item ->
                    val weight = item.getValue(FIELD_WEIGHT)
                    val time = item.getTimestamp(TimeUnit.MILLISECONDS)
                    calendar.timeInMillis = time
                    val userInformation = userUseCase.getWeightOfUserByTime(calendar.time)
                    userInformation?.let { u ->
                        u.weight = weight.asFloat().toDouble()
                        userUseCase.updateWeightForUser(u)
                    } ?: userUseCase.insertWeightForUser(
                        UserInformationModel(
                            weight = weight.asFloat().toDouble(),
                            time = calendar.time,
                            userId = userId!!
                        )
                    )
                }
            }
            syncGoogleFitData = false
            syncGoogleFitLiveData.value = true
            showLoading(isShowLoading = false)
        }
    }
}