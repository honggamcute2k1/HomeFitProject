package com.example.fitnessproject.data.local.repository

import com.example.fitnessproject.data.local.entity.Topic
import com.example.fitnessproject.data.local.entity.TopicDetail
import com.example.fitnessproject.data.local.entity.TopicDetailSelected
import com.example.fitnessproject.data.local.entity.TopicSelected
import java.util.*

interface TopicRepository {
    suspend fun getAllTopic(): List<Topic>
    suspend fun getAllTopicDetail(idTopic: Int): List<TopicDetail>
    suspend fun getTopicDetailById(idDetail: Int): TopicDetail
    suspend fun insertTopicSelectedDetail(topicDetailSelected: TopicDetailSelected)
    suspend fun getTopicSelectedDetailByTime(time: Date, idTopicDetail: Int): TopicDetailSelected?
    suspend fun getTopicSelectedDetailInDay(time: Date): List<TopicDetailSelected>
    suspend fun getTopicSelectedById(idTopic: Int): TopicSelected?
    suspend fun insertTopicSelected(topicSelected: TopicSelected)
    suspend fun getTopicDetailSelectedInTime(
        startTime: Date,
        endTime: Date
    ): List<TopicDetailSelected>
}