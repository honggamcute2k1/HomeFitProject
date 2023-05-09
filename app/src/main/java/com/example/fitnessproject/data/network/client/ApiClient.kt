package com.example.fitnessproject.data.network.client

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {
    companion object {
        @Volatile
        private var retrofit: Retrofit? = null

        @Volatile
        private var apiService: ApiService? = null

        private fun getRetrofit(): Retrofit {
            return retrofit ?: synchronized(this) {
                val re = Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/nguyenngoclinhbkhn/FitnessProject/master/")
//                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofit = re
                re
            }
        }

        fun getApiClient(): ApiService {
            return apiService ?: synchronized(this) {
                val api = getRetrofit().create(ApiService::class.java)
                apiService = api
                api
            }
        }
    }
}