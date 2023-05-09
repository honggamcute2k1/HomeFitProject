package com.example.fitnessproject.domain.usecase.main

import com.example.fitnessproject.data.local.entity.TopicDetailSelected
import com.example.fitnessproject.data.local.entity.TopicSelected
import com.example.fitnessproject.data.local.repository.TopicRepository
import com.example.fitnessproject.data.network.entity.BmiItem
import com.example.fitnessproject.data.network.repository.ApiRepository
import com.example.fitnessproject.domain.model.LevelBMI
import com.example.fitnessproject.domain.model.TopicDetailModel
import com.example.fitnessproject.domain.model.TopicDetailSelectedModel
import com.example.fitnessproject.domain.model.TopicModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MainUseCaseImpl(
    private val topicRepository: TopicRepository,
    private val apiRepository: ApiRepository
) : MainUseCase {
    override suspend fun getAllTopic(): List<TopicModel> {
        return topicRepository.getAllTopic().map {
            TopicModel.toTopicModel(it)
        }
    }

    override suspend fun getAllTopicById(idTopic: Int): List<TopicDetailModel> {
        return withContext(Dispatchers.IO) {
            topicRepository.getAllTopicDetail(idTopic).map {
                TopicDetailModel.toTopicDetailModel(it)
            }
        }
    }

    override suspend fun getTopicDetailById(idDetail: Int): TopicDetailModel {
        return withContext(Dispatchers.IO) {
            val topicDetail = topicRepository.getTopicDetailById(idDetail)
            TopicDetailModel.toTopicDetailModel(topicDetail)
        }
    }

    override suspend fun getTopicSelectedById(idTopic: Int): TopicSelected? {
        return withContext(Dispatchers.IO) {
            topicRepository.getTopicSelectedById(idTopic)
        }
    }

    override suspend fun insertTopicSelected(topicSelected: TopicSelected) {
        return withContext(Dispatchers.IO) {
            topicRepository.insertTopicSelected(topicSelected)
        }
    }

    override suspend fun getTopicSelectedDetailByDay(
        day: Date,
        idTopicDetail: Int
    ): TopicDetailSelectedModel? {
        return withContext(Dispatchers.IO) {
            val topicSelectedDetail =
                topicRepository.getTopicSelectedDetailByTime(day, idTopicDetail)
            topicSelectedDetail?.let { TopicDetailSelectedModel.toTopicDetailSelectedModel(it) }
        }
    }

    override suspend fun getTopicSelectedDetailInDay(day: Date): List<TopicDetailSelectedModel> {
        return withContext(Dispatchers.IO) {
            topicRepository.getTopicSelectedDetailInDay(day)
                .map { TopicDetailSelectedModel.toTopicDetailSelectedModel(it) }
        }
    }

    override suspend fun insertTopicSelectedDetail(topicDetailSelectedModel: TopicDetailSelectedModel) {
        val topicDetailSelected = TopicDetailSelected(
            state = topicDetailSelectedModel.state,
            topicDetailId = topicDetailSelectedModel.topicDetailId,
            topicSelectedId = topicDetailSelectedModel.topicSelectedId,
            timeDoIt = topicDetailSelectedModel.timeDoIt
        )
        return withContext(Dispatchers.IO) {
            topicRepository.insertTopicSelectedDetail(topicDetailSelected)
        }
    }

    override suspend fun getTopicDetailSelectedInTime(
        startTime: Date,
        endTime: Date
    ): List<TopicDetailSelectedModel> {
        return topicRepository.getTopicDetailSelectedInTime(startTime, endTime).map {
            TopicDetailSelectedModel.toTopicDetailSelectedModel(it)
        }
    }

    override suspend fun getBMIResponse(levelBMI: LevelBMI): BmiItem {
        return withContext(Dispatchers.IO) {
            apiRepository.getBMIResponse().items.first { it.level == levelBMI.level }

        }
    }
}