package com.example.fitnessproject.data.network.entity

import com.google.gson.Gson

class TopicResponse(
    val topic: List<Topic>
) {
    companion object {
        fun fromJson(json: String): TopicResponse {
            return Gson().fromJson(json, TopicResponse::class.java)
        }
    }
}

class Topic(
    val id: Int,
    val name: String,
    val type: Int,
    val items: List<TopicDetail>
)

data class TopicDetail(
    val name: String,
    val description: String,
    val idVideo: String,
    val tutorial: String
)