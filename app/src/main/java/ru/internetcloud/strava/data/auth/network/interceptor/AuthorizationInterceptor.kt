package ru.internetcloud.strava.data.auth.network.interceptor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.internetcloud.strava.domain.token.TokenRepository

class AuthorizationInterceptor(
    private val tokenRepository: TokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            chain.request()
                .addTokenHeader()
                .let {
                    chain.proceed(it)
                }
        }
    }

    private suspend fun Request.addTokenHeader(): Request {
        return newBuilder()
            .apply {
                withContext(Dispatchers.IO) {
                    val token = tokenRepository.getTokenData().accessToken
                    if (token != null) {
                        header(AUTH_HEADER_NAME, token.withBearer())
                    }
                }
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"

    companion object {
        private const val AUTH_HEADER_NAME = "Authorization"
    }
}
