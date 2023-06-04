package ru.internetcloud.strava.presentation.training.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

class TrainingDetailViewModelFactory(
    private val id: Long,
    private val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(TrainingDetailViewModel::class.java)) {
            return TrainingDetailViewModel(id = id, app = app, extras.createSavedStateHandle()) as T
        }
        throw IllegalStateException("Unknown view model class - $modelClass")
    }
}
