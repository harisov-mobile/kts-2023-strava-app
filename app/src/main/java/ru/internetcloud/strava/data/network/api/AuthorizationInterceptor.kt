package ru.internetcloud.strava.data.network.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.internetcloud.strava.presentation.auth.TokenStorage

class AuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .addTokenHeader()
            .let {
                chain.proceed(it)
            }
    }

    private fun Request.addTokenHeader(): Request {
        return newBuilder()
            .apply {
                val token = TokenStorage.accessToken
                if (token != null) {
                    header(AUTH_HEADER_NAME, token.withBearer())
                }
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"

    companion object {
        private const val AUTH_HEADER_NAME = "Authorization"
    }
}
