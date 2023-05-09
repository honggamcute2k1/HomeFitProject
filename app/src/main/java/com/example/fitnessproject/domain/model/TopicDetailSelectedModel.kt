package com.example.fitnessproject.domain.model

import com.example.fitnessproject.data.local.entity.TopicDetailSelected
import java.util.*

data class TopicDetailSelectedModel(
    var id: Int? = null,
    val state: Int,
    val topicDetailId: Int,
    val topicSelectedId: Int,
    val timeDoIt: Date
) {
    companion object {
        fun toTopicDetailSelectedModel(src: TopicDetailSelected): TopicDetailSelectedModel {
            return TopicDetailSelectedModel(
                id = src.id,
                state = src.state,
                topicDetailId = src.topicDetailId,
                topicSelectedId = src.topicSelectedId,
                timeDoIt = src.timeDoIt
            )
        }
    }
}