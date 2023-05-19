package ru.internetcloud.strava.domain.internet

import android.app.Application
import kotlinx.coroutines.flow.Flow

interface InternetStatusRepository {

    fun observeInternetChange(application: Application): Flow<Boolean>
}
