package com.example.fitnessproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitnessproject.data.local.entity.TopicDetail
import com.example.fitnessproject.data.local.entity.TopicDetailSelected
import java.util.*

@Dao
interface TopicDetailDao {
    @Query("SELECT * FROM detail_topic WHERE id_topic = :idTopic")
    fun getAllTopicDetail(idTopic: Int): List<TopicDetail>

    @Query("SELECT * FROM detail_topic WHERE id_detail = :idDetail")
    fun getTopicDetail(idDetail: Int): TopicDetail

    @Insert
    fun insertTopicDetailSelected(topicDetailSelected: TopicDetailSelected)

    @Query("SELECT * FROM topic_detail_selected WHERE topic_detail_id = :topicDetailId AND time_do_it = :date")
    fun getTopicDetailSelectedByDay(date: Date, topicDetailId: Int): TopicDetailSelected?

    @Query("SELECT * FROM topic_detail_selected WHERE time_do_it = :date")
    fun getTopicDetailSelectedInDay(date: Date): List<TopicDetailSelected>

    @Query("SELECT * FROM topic_detail_selected WHERE time_do_it BETWEEN :startTime AND :endTime")
    fun getTopicDetailSelectedInTime(startTime: Date, endTime: Date): List<TopicDetailSelected>
}