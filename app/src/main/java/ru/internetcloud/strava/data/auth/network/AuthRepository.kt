package ru.internetcloud.strava.data.auth.network

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

interface AuthRepository {

    fun getAuthRequest(): AuthorizationRequest

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    )

    suspend fun logout()
}
