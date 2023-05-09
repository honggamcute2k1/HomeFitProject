package com.example.fitnessproject.ui.activities.videodetail

import android.app.Application
import com.example.fitnessproject.domain.model.State
import com.example.fitnessproject.domain.model.TopicDetailModel
import com.example.fitnessproject.domain.model.TopicDetailSelectedModel
import com.example.fitnessproject.ui.activities.topicdetail.TopicDetailViewModel
import kotlinx.coroutines.launch
import java.util.*

class VideoDetailViewModel(application: Application) : TopicDetailViewModel(application) {

    fun getListTopicDetail(model: TopicDetailModel) {
        showLoading(isShowLoading = true)
        myScope.launch {
            val list = mainUseCase.getAllTopicById(idTopic = model.idTopic)
            val topicSelectedDetail =
                mainUseCase.getTopicSelectedDetailByDay(Calendar.getInstance().time, model.idDetail)
            if (topicSelectedDetail == null) {
                val topicSelected = mainUseCase.getTopicSelectedById(model.idTopic)
                val topicSelectedModel = TopicDetailSelectedModel(
                    state = State.DONE.state,
                    topicDetailId = model.idDetail,
                    timeDoIt = Calendar.getInstance().time,
                    topicSelectedId = topicSelected!!.id!!
                )
                mainUseCase.insertTopicSelectedDetail(topicSelectedModel)
            }
            topicDetailLiveData.value = list
        }
    }

    fun insertTopicSelectedDetail(model: TopicDetailModel) {
        showLoading(isShowLoading = true)
        myScope.launch {
            val topicSelectedDetail =
                mainUseCase.getTopicSelectedDetailByDay(Calendar.getInstance().time, model.idDetail)
            if (topicSelectedDetail == null) {
                val topicSelected = mainUseCase.getTopicSelectedById(model.idTopic)
                val topicSelectedModel = TopicDetailSelectedModel(
                    state = State.DONE.state,
                    topicDetailId = model.idDetail,
                    timeDoIt = Calendar.getInstance().time,
                    topicSelectedId = topicSelected!!.id!!
                )
                mainUseCase.insertTopicSelectedDetail(topicSelectedModel)
            }
            showLoading(isShowLoading = false)
        }
    }
}