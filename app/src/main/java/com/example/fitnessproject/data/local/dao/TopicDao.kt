package com.example.fitnessproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitnessproject.data.local.entity.Topic
import com.example.fitnessproject.data.local.entity.TopicSelected

@Dao
interface TopicDao {
    @Query("SELECT * FROM topic")
    fun getAllTopic(): List<Topic>

    @Query("SELECT * FROM topic_selected WHERE id_topic = :idTopic LIMIT 1")
    fun getTopicSelectedById(idTopic: Int): TopicSelected?

    @Insert
    fun insertTopicSelected(topicSelected: TopicSelected)
}