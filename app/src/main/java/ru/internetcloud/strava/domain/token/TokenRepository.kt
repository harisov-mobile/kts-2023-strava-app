package ru.internetcloud.strava.domain.token

import ru.internetcloud.strava.domain.token.model.TokensModel

interface TokenRepository {

    suspend fun isAuthorized(): Boolean

    suspend fun getTokenData(): TokensModel

    suspend fun saveTokenData(tokensModel: TokensModel)

    suspend fun clearTokenData()
}
