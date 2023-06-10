package ru.internetcloud.strava.data.auth.network

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest
import ru.internetcloud.strava.data.token.TokenSharedPreferencesStorage
import ru.internetcloud.strava.domain.token.model.TokensModel

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
        TokenSharedPreferencesStorage.saveTokenData(
            TokensModel(
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
                idToken = tokens.idToken
            )
        )
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return AppAuth.getEndSessionRequest()
    }

    fun logout() {
        TokenSharedPreferencesStorage.clearTokenData()
    }
}
