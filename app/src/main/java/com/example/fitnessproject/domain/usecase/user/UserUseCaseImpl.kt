package com.example.fitnessproject.domain.usecase.user

import com.example.fitnessproject.data.local.entity.User
import com.example.fitnessproject.data.local.entity.UserInformation
import com.example.fitnessproject.data.local.repository.UserRepository
import com.example.fitnessproject.domain.model.UserInformationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class UserUseCaseImpl(private val userRepository: UserRepository) : UserUseCase {
    override suspend fun insertUser(user: User) {
        return userRepository.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        return withContext(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    override suspend fun getAllUser(): List<User> {
        return userRepository.getAllUser()
    }

    override suspend fun insertWeightForUser(userInformation: UserInformationModel) {
        return withContext(Dispatchers.IO) {
            val uI = UserInformation(
                weight = userInformation.weight,
                time = userInformation.time,
                userId = userInformation.userId
            )
            userRepository.insertWeightForUser(uI)
        }
    }

    override suspend fun updateWeightForUser(userInformation: UserInformationModel) {
        return withContext(Dispatchers.IO) {
            val uI = UserInformation(
                id = userInformation.id,
                weight = userInformation.weight,
                time = userInformation.time,
                userId = userInformation.userId
            )
            userRepository.updateWeightForUser(uI)
        }
    }

    override suspend fun getWeightOfUserByTime(time: Date): UserInformationModel? {
        return withContext(Dispatchers.IO) {
            val uI = userRepository.getWeightOfUserByTime(time)
            UserInformationModel.toUserInformationModel(uI)
        }
    }

    override suspend fun getWeightOfUserInTime(
        startTime: Date,
        endTime: Date
    ): List<UserInformationModel?> {
        return withContext(Dispatchers.IO) {
            userRepository.getWeightOfUserInTime(startTime, endTime).map {
                UserInformationModel.toUserInformationModel(it)
            }
        }
    }
}