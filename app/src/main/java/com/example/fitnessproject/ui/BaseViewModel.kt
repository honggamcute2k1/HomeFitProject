package com.example.fitnessproject.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessproject.FitnessApplication
import com.example.fitnessproject.data.exception.MyException
import kotlinx.coroutines.*

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val sharePreference = (application as FitnessApplication).sharePreference
    lateinit var myScope: CoroutineScope
    lateinit var handlerException: CoroutineExceptionHandler
    val loadingData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String>()

    fun showLoading(isShowLoading: Boolean) {
        loadingData.value = isShowLoading
    }

    open fun onCreate() {
        handlerException = CoroutineExceptionHandler { _, exception ->
            Log.e("TAG", "Exception ${exception.toString()}")
            if (exception is MyException) {
                errorLiveData.value = exception.error.error
            } else {
                errorLiveData.value = exception.toString()
            }
        }
        myScope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handlerException)
    }

    fun onStart() {
    }

    fun onStop() {
        myScope.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        myScope.cancel()
    }
}
