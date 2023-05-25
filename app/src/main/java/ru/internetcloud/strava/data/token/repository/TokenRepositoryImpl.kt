package ru.internetcloud.strava.data.token.repository

import ru.internetcloud.strava.data.auth.network.TokenStorage
import ru.internetcloud.strava.data.token.TokenSharedPreferencesStorage
import ru.internetcloud.strava.domain.token.TokenRepository
import ru.internetcloud.strava.domain.token.model.TokensModel

class TokenRepositoryImpl : TokenRepository {

    override fun getToken(): TokensModel {
        return TokenSharedPreferencesStorage.getTokenData()
    }

    override fun rememberToken(tokensModel: TokensModel) {
        TokenStorage.accessToken = tokensModel.accessToken
        TokenStorage.refreshToken = tokensModel.refreshToken
        TokenStorage.idToken = tokensModel.idToken
    }
}
