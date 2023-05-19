package ru.internetcloud.strava.presentation.main.composable

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import ru.internetcloud.strava.data.internet.repository.InternetStatusRepositoryImpl
import ru.internetcloud.strava.domain.internet.usecase.GetInternetStatusUseCase

class MainScreenViewModel(private val app: Application) : ViewModel() {

    private val internetStatusRepository = InternetStatusRepositoryImpl()
    private val getInternetStatusUseCase = GetInternetStatusUseCase(internetStatusRepository)

    val internetConnectionAvailable: Flow<Boolean>
        get() = fetchInternetChange()

    private fun fetchInternetChange(): Flow<Boolean> {
        return getInternetStatusUseCase.observeInternetChange(app)
    }
}
