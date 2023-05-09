package com.example.fitnessproject.data.local.repository

import com.example.fitnessproject.data.local.entity.User
import com.example.fitnessproject.data.local.entity.UserInformation
import java.util.*

interface UserRepository {
    suspend fun insertUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun getAllUser(): List<User>

    suspend fun insertWeightForUser(userInformation: UserInformation)

    suspend fun updateWeightForUser(userInformation: UserInformation)

    suspend fun getWeightOfUserByTime(time: Date): UserInformation?

    suspend fun getWeightOfUserInTime(startTime: Date, endTime: Date): List<UserInformation>

}