package com.example.fitnessproject.ui.activities.topicdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.fitnessproject.FitnessApplication
import com.example.fitnessproject.data.local.entity.TopicSelected
import com.example.fitnessproject.domain.model.TopicDetailModel
import com.example.fitnessproject.domain.usecase.main.MainUseCase
import com.example.fitnessproject.domain.usecase.main.MainUseCaseImpl
import com.example.fitnessproject.ui.BaseViewModel
import kotlinx.coroutines.launch

open class TopicDetailViewModel(application: Application) : BaseViewModel(application) {
    val mainUseCase: MainUseCase =
        MainUseCaseImpl(
            (application as FitnessApplication).topicRepository,
            application.apiRepository
        )

    val topicDetailLiveData = MutableLiveData<List<TopicDetailModel>>()
    val detailLiveData = MutableLiveData<TopicDetailModel>()


    fun getListTopicDetailOfTopic(topicId: Int) {
        showLoading(isShowLoading = true)
        myScope.launch {
            val topicListDetail = mainUseCase.getAllTopicById(idTopic = topicId)
            val topicSelected = mainUseCase.getTopicSelectedById(idTopic = topicId)
            val idUser = sharePreference.getUserId()
            if (topicSelected == null) {
                mainUseCase.insertTopicSelected(
                    TopicSelected(
                        idTopic = topicId,
                        idUser = idUser
                    )
                )
            }
            topicDetailLiveData.value = topicListDetail
            showLoading(isShowLoading = false)
        }
    }

    fun getTopicDetail(topicDetailModel: TopicDetailModel) {
        showLoading(isShowLoading = true)
        myScope.launch {
            val model = mainUseCase.getTopicDetailById(topicDetailModel.idDetail)
            detailLiveData.value = model
            showLoading(isShowLoading = false)
        }
    }
}