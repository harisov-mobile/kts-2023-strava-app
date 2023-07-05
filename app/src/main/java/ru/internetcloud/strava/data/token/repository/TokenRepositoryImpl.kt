package ru.internetcloud.strava.data.token.repository

import ru.internetcloud.strava.data.token.TokenSharedPreferencesStorage
import ru.internetcloud.strava.domain.token.TokenRepository
import ru.internetcloud.strava.domain.token.model.TokensModel

class TokenRepositoryImpl(
    private val tokenSharedPreferencesStorage: TokenSharedPreferencesStorage
) : TokenRepository {

    override suspend fun isAuthorized(): Boolean {
        return tokenSharedPreferencesStorage.isAuthorized()
    }

    override suspend fun getTokenData(): TokensModel {
        return tokenSharedPreferencesStorage.getTokenData()
    }

    override suspend fun saveTokenData(tokensModel: TokensModel) {
        tokenSharedPreferencesStorage.saveTokenData(tokensModel)
    }

    override suspend fun clearTokenData() {
        tokenSharedPreferencesStorage.clearTokenData()
    }
}
