package com.example.fitnessproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val userId: Int? = null,

    @ColumnInfo(name = "user_name")
    var name: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "thumbnail")
    var thumbnail: String? = null,

    @ColumnInfo(name = "gender")
    var gender: Int,

    @ColumnInfo(name = "height")
    var height: Double? = null,

    @ColumnInfo(name = "weight")
    var weight: Double? = null,

    @ColumnInfo(name = "born")
    var born: Int,

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String,

    @ColumnInfo(name = "full_name")
    var fullName: String
)