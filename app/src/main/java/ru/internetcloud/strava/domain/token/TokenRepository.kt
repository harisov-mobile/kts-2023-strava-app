package ru.internetcloud.strava.domain.token

import ru.internetcloud.strava.domain.token.model.TokensModel

interface TokenRepository {

    fun getToken(): TokensModel

    fun rememberToken(tokensModel: TokensModel)
}
