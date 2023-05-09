package com.example.fitnessproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_topic")
data class TopicDetail(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_detail")
    val idDetail: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "id_video")
    val idVideo: String,

    @ColumnInfo(name = "tutorial")
    val tutorial: String,

    @ColumnInfo(name = "id_topic")
    val idTopic: Int
)