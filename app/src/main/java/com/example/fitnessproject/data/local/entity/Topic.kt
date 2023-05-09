package com.example.fitnessproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic")
data class Topic(
    @PrimaryKey
    @ColumnInfo(name = "id_topic")
    val idTopic: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: Int
) {
}