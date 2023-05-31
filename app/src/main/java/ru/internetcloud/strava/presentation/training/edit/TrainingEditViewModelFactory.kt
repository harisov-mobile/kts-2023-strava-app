package ru.internetcloud.strava.presentation.training.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

class TrainingEditViewModelFactory(
    private val id: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(TrainingEditViewModel::class.java)) {
            return TrainingEditViewModel(id = id, extras.createSavedStateHandle()) as T
        }
        throw IllegalStateException("Unknown view model class - $modelClass")
    }
}
