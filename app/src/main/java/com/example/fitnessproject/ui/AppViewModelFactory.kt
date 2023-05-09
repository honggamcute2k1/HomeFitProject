package com.example.fitnessproject.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppViewModelFactory(private val creators: Map<Class<out ViewModel>, ViewModel>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.asIterable()
            .firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
        ?: throw IllegalArgumentException("Unknown ViewModel class")
        return try {
            creator as T
        } catch (e: Exception) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}