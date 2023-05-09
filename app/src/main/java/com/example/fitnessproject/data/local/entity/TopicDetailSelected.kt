package com.example.fitnessproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.fitnessproject.data.local.TimeConverter
import java.util.*

@Entity(tableName = "topic_detail_selected")
data class TopicDetailSelected(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "state")
    val state: Int,

    @ColumnInfo(name = "topic_detail_id")
    val topicDetailId: Int,

    @ColumnInfo(name = "topic_selected_id")
    val topicSelectedId: Int,

    @ColumnInfo(name = "time_do_it")
    @TypeConverters(TimeConverter::class)
    val timeDoIt: Date
)