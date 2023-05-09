package com.example.fitnessproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitnessproject.data.local.entity.UserInformation
import java.util.*

@Dao
interface WeightDao {
    @Insert
    fun insertWeight(userInformation: UserInformation)

    @Update
    fun updateWeight(userInformation: UserInformation)

    @Query("SELECT * FROM information WHERE time = :time")
    fun getUserInformationByTime(time: Date): UserInformation?

    @Query("SELECT * FROM information WHERE time BETWEEN :startDate AND :endDate ORDER BY time")
    fun getUserInformationInTime(startDate: Date, endDate: Date): List<UserInformation>
}