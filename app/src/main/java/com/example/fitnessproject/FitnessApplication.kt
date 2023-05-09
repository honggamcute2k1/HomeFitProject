package com.example.fitnessproject

import android.app.Application
import com.example.fitnessproject.data.local.database.FitnessDatabase
import com.example.fitnessproject.data.local.repository.*
import com.example.fitnessproject.data.network.client.ApiClient
import com.example.fitnessproject.data.network.repository.ApiRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FitnessApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database: FitnessDatabase by lazy { FitnessDatabase.getDatabase(this, applicationScope) }
    val sharePreference: SharePreference by lazy { SharePreferenceImpl.getSharePreference(this) }
    val userRepository by lazy { UserRepositoryImpl(database.userDao(), database.weightDao()) }
    val versionRepository by lazy { VersionRepositoryImpl(database.versionDao()) }
    val topicRepository by lazy {
        TopicRepositoryImpl(
            database.topicDao(),
            database.topicDetailDao()
        )
    }
    val apiRepository by lazy { ApiRepositoryImpl(ApiClient.getApiClient()) }


    override fun onCreate() {
        super.onCreate()
        getScreenSize()
    }

    private fun getScreenSize() {
        resources.displayMetrics.run {
            screenDensity = this.density
        }
    }
}