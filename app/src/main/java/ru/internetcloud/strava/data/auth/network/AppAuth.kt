package ru.internetcloud.strava.data.auth.network

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import ru.internetcloud.strava.domain.token.model.TokensModel

interface AppAuth {
    fun getAuthRequest(): AuthorizationRequest

    fun getRefreshTokenRequest(refreshToken: String): TokenRequest

    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    ): TokensModel
}
