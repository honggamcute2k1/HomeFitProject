package com.example.fitnessproject.data.local.repository

import com.example.fitnessproject.data.local.dao.TopicDao
import com.example.fitnessproject.data.local.dao.TopicDetailDao
import com.example.fitnessproject.data.local.entity.Topic
import com.example.fitnessproject.data.local.entity.TopicDetail
import com.example.fitnessproject.data.local.entity.TopicDetailSelected
import com.example.fitnessproject.data.local.entity.TopicSelected
import java.util.*

class TopicRepositoryImpl(
    private val topicDao: TopicDao,
    private val topicDetailDao: TopicDetailDao
) : TopicRepository {
    override suspend fun getAllTopic(): List<Topic> {
        return topicDao.getAllTopic()
    }

    override suspend fun getAllTopicDetail(idTopic: Int): List<TopicDetail> {
        return topicDetailDao.getAllTopicDetail(idTopic)
    }

    override suspend fun getTopicDetailById(idDetail: Int): TopicDetail {
        return topicDetailDao.getTopicDetail(idDetail)
    }

    override suspend fun insertTopicSelectedDetail(topicDetailSelected: TopicDetailSelected) {
        return topicDetailDao.insertTopicDetailSelected(topicDetailSelected)
    }

    override suspend fun getTopicSelectedById(idTopic: Int): TopicSelected? {
        return topicDao.getTopicSelectedById(idTopic)
    }

    override suspend fun insertTopicSelected(topicSelected: TopicSelected) {
        return topicDao.insertTopicSelected(topicSelected)
    }

    override suspend fun getTopicSelectedDetailByTime(
        time: Date,
        idTopicDetail: Int
    ): TopicDetailSelected? {
        return topicDetailDao.getTopicDetailSelectedByDay(
            date = time,
            topicDetailId = idTopicDetail
        )
    }

    override suspend fun getTopicSelectedDetailInDay(time: Date): List<TopicDetailSelected> {
        return topicDetailDao.getTopicDetailSelectedInDay(
            date = time
        )
    }

    override suspend fun getTopicDetailSelectedInTime(
        startTime: Date,
        endTime: Date
    ): List<TopicDetailSelected> {
        return topicDetailDao.getTopicDetailSelectedInTime(startTime, endTime)
    }
}