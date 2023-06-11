package ru.internetcloud.strava.data.auth.network.interceptor

import java.net.HttpURLConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthorizationService
import okhttp3.Interceptor
import okhttp3.Response
import ru.internetcloud.strava.data.auth.network.AppAuth
import ru.internetcloud.strava.domain.token.TokenRepository
import ru.internetcloud.strava.domain.token.UnauthorizedHandler

class AuthorizationFailedInterceptor(
    private val authorizationService: AuthorizationService,
    private val tokenRepository: TokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val oldResponse = chain.proceed(oldRequest)

        return if (oldResponse.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            val tokenRefreshed = refreshToken()

            if (tokenRefreshed) {
                oldResponse.close() // we need to close response to be able to start new request
                val newRequest = oldRequest.newBuilder().build() // сработает другой интерцептор,
                // который добавит обновленный токен, поэтому здесь не надо токен добавлять
                chain.proceed(newRequest)
            } else {
                oldResponse
            }
        } else {
            oldResponse
        }
    }

    private fun refreshToken(): Boolean {
        val tokenRefreshed = runBlocking {
            runCatching {
                withContext(Dispatchers.IO) {
                    val refreshRequest = AppAuth.getRefreshTokenRequest(
                        tokenRepository.getTokenData().refreshToken.orEmpty()
                    )
                    AppAuth.performTokenRequestSuspend(authorizationService, refreshRequest)
                }
            }
                .getOrNull()
                ?.let { tokens ->
                    withContext(Dispatchers.IO) {
                        tokenRepository.saveTokenData(tokens)
                    }
                    true
                } ?: false
        }

        if (!tokenRefreshed) {
            // не удалось обновить токен, произвести логаут
            runBlocking {
                UnauthorizedHandler.onUnauthorized()
            }
        }
        return tokenRefreshed
    }
}
