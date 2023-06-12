package ru.internetcloud.strava.data.auth.network

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import ru.internetcloud.strava.domain.token.TokenRepository
import ru.internetcloud.strava.domain.token.model.TokensModel

class AuthRepositoryImpl(
    private val tokenRepository: TokenRepository,
    private val appAuth: AppAuth
) : AuthRepository {

    override fun getAuthRequest(): AuthorizationRequest {
        return appAuth.getAuthRequest()
    }

    override suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    ) {
        val tokens = appAuth.performTokenRequestSuspend(authService, tokenRequest)
        // обмен кода на токен произошел успешно, сохраняем токены и завершаем авторизацию
        tokenRepository.saveTokenData(
            TokensModel(
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
                idToken = tokens.idToken
            )
        )
    }

    override suspend fun logout() {
        tokenRepository.clearTokenData()
    }
}
