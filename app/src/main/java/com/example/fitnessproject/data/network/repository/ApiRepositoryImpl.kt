package com.example.fitnessproject.data.network.repository

import com.example.fitnessproject.data.network.client.ApiService
import com.example.fitnessproject.data.network.entity.BmiResponse

class ApiRepositoryImpl(private val apiClient: ApiService) : ApiRepository {
    override suspend fun getBMIResponse(): BmiResponse {
        return apiClient.getBMIResponse()
    }
}