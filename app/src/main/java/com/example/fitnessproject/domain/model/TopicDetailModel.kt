package com.example.fitnessproject.domain.model

import android.os.Parcelable
import com.example.fitnessproject.data.local.entity.TopicDetail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopicDetailModel(
    val idDetail: Int,
    val name: String,
    val description: String,
    val idVideo: String,
    val tutorial: String,
    val idTopic: Int
) : Parcelable {
    companion object {
        fun toTopicDetailModel(src: TopicDetail): TopicDetailModel {
            return TopicDetailModel(
                src.idDetail,
                src.name,
                src.description,
                src.idVideo,
                src.tutorial,
                src.idTopic
            )
        }
    }
}