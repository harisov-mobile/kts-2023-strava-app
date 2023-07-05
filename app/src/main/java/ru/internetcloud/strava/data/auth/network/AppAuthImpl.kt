package ru.internetcloud.strava.data.auth.network

import android.net.Uri
import androidx.core.net.toUri
import kotlin.coroutines.suspendCoroutine
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import ru.internetcloud.strava.BuildConfig
import ru.internetcloud.strava.domain.token.model.TokensModel

class AppAuthImpl : AppAuth {

    private val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI),
        Uri.parse(AuthConfig.TOKEN_URI),
        null, // registration endpoint
        Uri.parse(AuthConfig.END_SESSION_URI)
    )

    override fun getAuthRequest(): AuthorizationRequest {
        val redirectUri = AuthConfig.CALLBACK_URL.toUri()

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            BuildConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    override fun getRefreshTokenRequest(refreshToken: String): TokenRequest {
        return TokenRequest.Builder(
            serviceConfiguration,
            BuildConfig.CLIENT_ID
        )
            .setGrantType(GrantTypeValues.REFRESH_TOKEN)
            .setScopes(AuthConfig.SCOPE)
            .setRefreshToken(refreshToken)
            .build()
    }

    override suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    ): TokensModel {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(tokenRequest, getClientAuthentication()) { response, ex ->
                when {
                    response != null -> {
                        // получение токена произошло успешно
                        val tokens = TokensModel(
                            accessToken = response.accessToken,
                            refreshToken = response.refreshToken,
                            idToken = response.idToken
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }
                    // получение токенов произошло неуспешно, показываем ошибку
                    ex != null -> { continuation.resumeWith(Result.failure(ex)) }
                    else -> error("unreachable")
                }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(BuildConfig.CLIENT_SECRET)
    }

    private object AuthConfig {
        const val AUTH_URI = "https://www.strava.com/oauth/mobile/authorize"
        const val TOKEN_URI = "https://www.strava.com/oauth/token"
        const val END_SESSION_URI = "https://www.strava.com/oauth/deauthorize"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val SCOPE = "read,read_all,profile:read_all,profile:write,activity:read_all,activity:write"

        // чтобы не "светить" Api ключи в публичном гит-хабе,
        // создан файл keystore.properties и в этот файл помещены значения CLIENT_ID и CLIENT_SECRET

        const val CALLBACK_URL = "ru.internetcloud.strava.oauth://internetcloud.ru/callback"
        const val LOGOUT_CALLBACK_URL = "ru.internetcloud.strava.oauth://internetcloud.ru/logout_callback"
    }
}
