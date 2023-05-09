package com.example.fitnessproject.data.network.client

import com.example.fitnessproject.data.network.entity.BmiResponse
import retrofit2.http.GET

interface ApiService {
    @GET("bmi.json")
    suspend fun getBMIResponse(): BmiResponse
}