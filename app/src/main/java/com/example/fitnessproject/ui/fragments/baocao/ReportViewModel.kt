package com.example.fitnessproject.ui.fragments.baocao

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.fitnessproject.FitnessApplication
import com.example.fitnessproject.data.network.entity.BmiItem
import com.example.fitnessproject.domain.model.*
import com.example.fitnessproject.domain.usecase.main.MainUseCase
import com.example.fitnessproject.domain.usecase.main.MainUseCaseImpl
import com.example.fitnessproject.domain.usecase.user.UserUseCase
import com.example.fitnessproject.domain.usecase.user.UserUseCaseImpl
import com.example.fitnessproject.ui.BaseViewModel
import kotlinx.coroutines.*
import java.util.*

class ReportViewModel(application: Application) : BaseViewModel(application) {
    private val userUserCase: UserUseCase =
        UserUseCaseImpl((application as FitnessApplication).userRepository)
    private val mainUseCase: MainUseCase =
        MainUseCaseImpl(
            (application as FitnessApplication).topicRepository,
            application.apiRepository
        )

    val isAddWeightLiveData = MutableLiveData<Boolean>()
    val weightListLiveData = MutableLiveData<List<UserInformationModel>>()
    val topicDetailSelectedLiveData = MutableLiveData<List<TopicDetailSelectedModel>>()
    val topicDetailLiveData = MutableLiveData<ArrayList<TopicDetailModel>>()
    val informationTimeLiveData = MutableLiveData<ArrayList<String>>()
    val gender: Gender? = null

    val loadingBMI = MutableLiveData<Boolean>()
    val updateInfoUser = MutableLiveData<Boolean>()
    val userInfoLiveData = MutableLiveData<Pair<Double?, Double?>>()
    val noBmiLiveData = MutableLiveData<Boolean>()
    val bmiLiveData = MutableLiveData<Pair<Double, BmiItem>>()

    var year: Int? = null
    var month: Int? = null

    override fun onCreate() {
        super.onCreate()
        getDataInTime()
        getInformationBMI()
    }

    fun insertOrUpdateWeight(weight: Double, time: Date) {
        showLoading(isShowLoading = true)
        myScope.launch {
            val user = withContext(Dispatchers.IO) {
                userUserCase.getAllUser().first()
            }
            val userId = user.userId
            val userInformation = userUserCase.getWeightOfUserByTime(time)
            userInformation?.let { u ->
                u.weight = weight
                userUserCase.updateWeightForUser(u)
            } ?: userUserCase.insertWeightForUser(
                UserInformationModel(
                    weight = weight,
                    time = time,
                    userId = userId!!
                )
            )
            isAddWeightLiveData.value = true
        }
    }

    fun getUserInformationInTime(
        year: Int? = null,
        month: Int? = null
    ) {
        showLoading(isShowLoading = true)
        myScope.launch {
            val startDate = Calendar.getInstance()
            val currentMonth = month ?: startDate.get(Calendar.MONTH)
            val currentYear = year ?: startDate.get(Calendar.YEAR)
            val startDay = 1
            startDate.set(currentYear, currentMonth, startDay)
            val endDay = startDate.getActualMaximum(Calendar.DAY_OF_MONTH)
            val endDate = Calendar.getInstance()
            endDate.set(currentYear, currentMonth, endDay)
            val infoList = userUserCase.getWeightOfUserInTime(
                startDate.time,
                endDate.time
            )
            val weightList = arrayListOf<String>()
            val calendar = Calendar.getInstance()
            infoList.filterNotNull().map {
                calendar.time = it.time
                weightList.add(
                    it.weight.toString().plus(":").plus(calendar.get(Calendar.DAY_OF_MONTH))
                )
            }
            informationTimeLiveData.value = weightList
            showLoading(isShowLoading = false)
        }
    }

    fun getDataInTime(
        year: Int? = null,
        month: Int? = null
    ) {
        showLoading(isShowLoading = true)
        val startDate = Calendar.getInstance()
        val currentMonth = month ?: startDate.get(Calendar.MONTH)
        val currentYear = year ?: startDate.get(Calendar.YEAR)
        this.month = currentMonth
        this.year = currentYear
        val startDay = 1
        startDate.set(currentYear, currentMonth, startDay)
        val endDay = startDate.getActualMaximum(Calendar.DAY_OF_MONTH)
        val endDate = Calendar.getInstance()
        endDate.set(currentYear, currentMonth, endDay)
        myScope.launch {
            val weightDeferred = async(Dispatchers.IO) {
                delay(1500)
                userUserCase.getWeightOfUserInTime(
                    startDate.time,
                    endDate.time
                )
            }
            val topicDetailSelectedDeferred = async(Dispatchers.IO) {
                mainUseCase.getTopicDetailSelectedInTime(
                    startDate.time,
                    endDate.time
                )
            }
            weightListLiveData.value = weightDeferred.await().filterNotNull()
            topicDetailSelectedLiveData.value =
                topicDetailSelectedDeferred.await().distinctBy { it.timeDoIt }
            showLoading(isShowLoading = false)
        }
    }

    fun getInformation() {
        myScope.launch {
            val user = withContext(Dispatchers.IO) {
                userUserCase.getAllUser().first()
            }
            userInfoLiveData.value = user.weight to user.height
        }
    }

    fun updateBMI(height: Float, weight: Float) {
        myScope.launch {
            val user = withContext(Dispatchers.IO) {
                userUserCase.getAllUser().first()
            }
            user.weight = weight.toDouble()
            user.height = height.toDouble()
            userUserCase.updateUser(user)
            updateInfoUser.value = true
        }
    }

    fun getInformationBMI() {
        loadingBMI.value = true
        myScope.launch {
            delay(1500)
            val user = withContext(Dispatchers.IO) {
                userUserCase.getAllUser().first()
            }
            val weight = user.weight
            val height = user.height
            if (weight == null || height == null) {
                noBmiLiveData.value = true
            } else {
                val bmi = weight / (height * 2 / 100)
                val level = getLevelBMI(bmi)
                val bmiItem = mainUseCase.getBMIResponse(level)
                bmiLiveData.value = bmi to bmiItem
            }
            loadingBMI.value = false
        }
    }

    private fun getLevelBMI(value: Double): LevelBMI {
        if (value < 16.0) return LevelBMI.GAY_3
        if (value in 16.0..16.99) return LevelBMI.GAY_2
        if (value >= 17.0 && value < 18.05) return LevelBMI.GAY_1
        if (value in 18.05..24.9) return LevelBMI.BINH_THUONG
        if (value in 25.0..29.9) return LevelBMI.THUA_CAN
        if (value in 30.0..34.9) return LevelBMI.BEO_1
        return if (value in 35.0..39.9) LevelBMI.BEO_2
        else LevelBMI.BEO_3
    }

    fun getCurrentDateString(): String {
        val calendar = Calendar.getInstance()
        return calendar.time.toDateString()
    }

    fun getGenderUser() = sharePreference.getGender()

    fun getListTopicDetailModel(byTime: Date) {
        showLoading(true)
        myScope.launch {
            val listTopicDetailSelectedModel = mainUseCase.getTopicSelectedDetailInDay(byTime)
            val idList = listTopicDetailSelectedModel.map { it.topicDetailId }
            val list = arrayListOf<TopicDetailModel>()
            idList.forEach { id ->
                val topicDetailModel = withContext(Dispatchers.IO) {
                    mainUseCase.getTopicDetailById(id)
                }
                list.add(topicDetailModel)
            }
            topicDetailLiveData.value = list
            showLoading(false)
        }
    }
}