package com.example.fitnessproject.data.local.repository

import androidx.lifecycle.LiveData
import com.example.fitnessproject.data.local.dao.UserDao
import com.example.fitnessproject.data.local.dao.WeightDao
import com.example.fitnessproject.data.local.entity.User
import com.example.fitnessproject.data.local.entity.UserInformation
import java.util.*

class UserRepositoryImpl(private val userDao: UserDao, private val weightDao: WeightDao) :
    UserRepository {
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun getAllUser(): List<User> {
        return userDao.getAllUser()
    }

    override suspend fun insertWeightForUser(userInformation: UserInformation) {
        return weightDao.insertWeight(userInformation)
    }

    override suspend fun updateWeightForUser(userInformation: UserInformation) {
        return weightDao.updateWeight(userInformation)
    }

    override suspend fun getWeightOfUserByTime(time: Date): UserInformation? {
        return weightDao.getUserInformationByTime(time)
    }

    override suspend fun getWeightOfUserInTime(
        startTime: Date,
        endTime: Date
    ): List<UserInformation> {
        return weightDao.getUserInformationInTime(startTime, endTime)
    }

    suspend fun getAllUserTest(): LiveData<List<User>> {
        return userDao.getAllUserTest()
    }
}