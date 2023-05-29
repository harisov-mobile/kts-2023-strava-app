package ru.internetcloud.strava.presentation.training.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

class TrainingDetailViewModelFactory(
    private val id: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(TrainingDetailViewModel::class.java)) {
            return TrainingDetailViewModel(id = id, extras.createSavedStateHandle()) as T
        }
        throw IllegalStateException("Unkown view model class - $modelClass")
    }
}
