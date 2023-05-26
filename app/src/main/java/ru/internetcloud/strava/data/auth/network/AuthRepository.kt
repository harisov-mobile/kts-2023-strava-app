package ru.internetcloud.strava.data.auth.network

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest

class AuthRepository {

    fun getAuthRequest(): AuthorizationRequest {
        return AppAuth.getAuthRequest()
    }

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    ) {
        val tokens = AppAuth.performTokenRequestSuspend(authService, tokenRequest)
        // обмен кода на токен произошел успешно, сохраняем токены и завершаем авторизацию
        TokenStorage.accessToken = tokens.accessToken
        TokenStorage.refreshToken = tokens.refreshToken
        TokenStorage.idToken = tokens.idToken

        TokenStorage.saveTokenToPrefs()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return AppAuth.getEndSessionRequest()
    }

    fun corruptAccessToken() {
        TokenStorage.accessToken = "fake token"
    }

    fun logout() {
        TokenStorage.accessToken = null
        TokenStorage.refreshToken = null
        TokenStorage.idToken = null
    }
}
