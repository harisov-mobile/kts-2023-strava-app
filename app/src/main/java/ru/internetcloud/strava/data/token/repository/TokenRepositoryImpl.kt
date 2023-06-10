package ru.internetcloud.strava.data.token.repository

import ru.internetcloud.strava.data.token.TokenSharedPreferencesStorage
import ru.internetcloud.strava.domain.token.TokenRepository

class TokenRepositoryImpl : TokenRepository {

    override fun isAuthorized(): Boolean {
        return TokenSharedPreferencesStorage.isAuthorized()
    }
}
