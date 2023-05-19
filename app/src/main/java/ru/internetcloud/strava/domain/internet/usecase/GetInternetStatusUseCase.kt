package ru.internetcloud.strava.domain.internet.usecase

import android.app.Application
import kotlinx.coroutines.flow.Flow
import ru.internetcloud.strava.domain.internet.InternetStatusRepository

class GetInternetStatusUseCase(private val internetStatusRepository: InternetStatusRepository) {

    fun observeInternetChange(application: Application): Flow<Boolean> {
        return internetStatusRepository.observeInternetChange(application)
    }
}
