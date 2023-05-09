package com.example.fitnessproject.domain.usecase.main

import com.example.fitnessproject.data.local.entity.TopicSelected
import com.example.fitnessproject.data.network.entity.BmiItem
import com.example.fitnessproject.domain.model.LevelBMI
import com.example.fitnessproject.domain.model.TopicDetailModel
import com.example.fitnessproject.domain.model.TopicDetailSelectedModel
import com.example.fitnessproject.domain.model.TopicModel
import java.util.*

interface MainUseCase {
    suspend fun getAllTopic(): List<TopicModel>
    suspend fun getAllTopicById(idTopic: Int): List<TopicDetailModel>
    suspend fun getTopicDetailById(idDetail: Int): TopicDetailModel
    suspend fun insertTopicSelectedDetail(topicDetailSelectedModel: TopicDetailSelectedModel)
    suspend fun getTopicSelectedDetailByDay(
        day: Date,
        idTopicDetail: Int
    ): TopicDetailSelectedModel?

    suspend fun getTopicSelectedDetailInDay(
        day: Date
    ): List<TopicDetailSelectedModel>

    suspend fun getTopicSelectedById(idTopic: Int): TopicSelected?
    suspend fun insertTopicSelected(topicSelected: TopicSelected)
    suspend fun getTopicDetailSelectedInTime(
        startTime: Date,
        endTime: Date
    ): List<TopicDetailSelectedModel>

    suspend fun getBMIResponse(levelBMI: LevelBMI): BmiItem
}