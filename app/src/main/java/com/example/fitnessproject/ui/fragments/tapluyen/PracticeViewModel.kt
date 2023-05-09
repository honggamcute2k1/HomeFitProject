package com.example.fitnessproject.ui.fragments.tapluyen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.fitnessproject.FitnessApplication
import com.example.fitnessproject.domain.model.TopicModel
import com.example.fitnessproject.domain.usecase.main.MainUseCase
import com.example.fitnessproject.domain.usecase.main.MainUseCaseImpl
import com.example.fitnessproject.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PracticeViewModel(application: Application) : BaseViewModel(application) {
    private val mainUseCase: MainUseCase =
        MainUseCaseImpl(
            (application as FitnessApplication).topicRepository,
            application.apiRepository
        )

    val topicLiveData = MutableLiveData<List<TopicModel>>()

    override fun onCreate() {
        super.onCreate()
        getTopic()
    }

    private fun getTopic() {
        showLoading(isShowLoading = true)
        myScope.launch {
            delay(2000)
            val topicList = withContext(Dispatchers.IO) {
                mainUseCase.getAllTopic()
            }
            topicLiveData.value = topicList
            showLoading(isShowLoading = false)
        }
    }
}