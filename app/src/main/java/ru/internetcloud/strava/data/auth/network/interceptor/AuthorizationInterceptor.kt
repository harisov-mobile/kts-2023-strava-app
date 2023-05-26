package ru.internetcloud.strava.data.auth.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.internetcloud.strava.data.auth.network.TokenStorage
import timber.log.Timber

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
                Timber.tag("rustam").d("addTokenHeader, accessToken = $token")
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
