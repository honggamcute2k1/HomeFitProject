package com.example.fitnessproject.domain.model

import android.os.Parcelable
import com.example.fitnessproject.data.local.entity.Topic
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopicModel(
    val idTopic: Int,
    val name: String,
    val type: Int
) : Parcelable {
    companion object {
        fun toTopicModel(src: Topic): TopicModel {
            return TopicModel(
                src.idTopic,
                src.name,
                src.type
            )
        }
    }
}