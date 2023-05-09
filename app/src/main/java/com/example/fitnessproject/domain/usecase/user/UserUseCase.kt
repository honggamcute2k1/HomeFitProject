package com.example.fitnessproject.domain.usecase.user

import com.example.fitnessproject.data.local.entity.User
import com.example.fitnessproject.domain.model.UserInformationModel
import java.util.*

interface UserUseCase {
    suspend fun insertUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun getAllUser(): List<User>

    suspend fun insertWeightForUser(userInformation: UserInformationModel)

    suspend fun updateWeightForUser(userInformation: UserInformationModel)

    suspend fun getWeightOfUserByTime(time: Date): UserInformationModel?

    suspend fun getWeightOfUserInTime(startTime: Date, endTime: Date): List<UserInformationModel?>

}