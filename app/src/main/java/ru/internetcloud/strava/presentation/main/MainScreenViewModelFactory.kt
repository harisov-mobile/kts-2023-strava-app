package ru.internetcloud.strava.presentation.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainScreenViewModelFactory(
    private val app: Application,
    private val keyMessage: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
            return MainScreenViewModel(app, keyMessage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
