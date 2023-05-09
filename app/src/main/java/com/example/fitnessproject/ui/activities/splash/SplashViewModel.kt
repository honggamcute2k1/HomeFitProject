package com.example.fitnessproject.ui.activities.splash

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.fitnessproject.FitnessApplication
import com.example.fitnessproject.data.local.database.FitnessDatabase
import com.example.fitnessproject.domain.usecase.version.VersionUserCaseImpl
import com.example.fitnessproject.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SplashViewModel(application: Application) : BaseViewModel(application) {
    private val versionUserCase =
        VersionUserCaseImpl((application as FitnessApplication).versionRepository)

    val initSuccessLiveData = MutableLiveData<Boolean>()

    fun initDataBaseFirstTime() {
        myScope.launch {
            delay(1000)
            val version = withContext(Dispatchers.IO) {
                versionUserCase.getVersion()
            }
            initSuccessLiveData.value = version.version == FitnessDatabase.DATABASE_VERSION
        }
    }

    fun isSetupFirstTime() = sharePreference.isSetupFirstTime()
}