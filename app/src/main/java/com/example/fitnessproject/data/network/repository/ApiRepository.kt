package com.example.fitnessproject.data.network.repository

import com.example.fitnessproject.data.network.entity.BmiResponse

interface ApiRepository {
    suspend fun getBMIResponse(): BmiResponse
}