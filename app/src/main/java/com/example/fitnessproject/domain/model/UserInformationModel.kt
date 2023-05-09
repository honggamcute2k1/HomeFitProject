package com.example.fitnessproject.domain.model

import com.example.fitnessproject.data.local.entity.UserInformation
import java.util.*

data class UserInformationModel(
    val id: Int? = null,
    var weight: Double?,
    val time: Date,
    val userId: Int
) {
    companion object {
        fun toUserInformationModel(s: UserInformation?): UserInformationModel? {
            return s?.let { src ->
                UserInformationModel(
                    id = src.id,
                    weight = src.weight,
                    time = src.time,
                    userId = src.userId
                )
            }
        }
    }

    fun getDayInMonth(): String {
        val calendar = Calendar.getInstance()
        calendar.time = time
        return ""
    }
}