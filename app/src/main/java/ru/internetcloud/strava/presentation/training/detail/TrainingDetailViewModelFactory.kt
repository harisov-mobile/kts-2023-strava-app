package ru.internetcloud.strava.presentation.training.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TrainingDetailViewModelFactory(
    private val id: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingDetailViewModel::class.java)) {
            return TrainingDetailViewModel(id = id) as T
        }
        throw IllegalStateException("Unkown view model class - $modelClass")
    }
}
