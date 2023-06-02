package ru.internetcloud.strava.data.common

import android.content.Context
import net.openid.appauth.AuthorizationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.internetcloud.strava.data.auth.network.TokenStorage
import ru.internetcloud.strava.data.auth.network.interceptor.AuthorizationFailedInterceptor
import ru.internetcloud.strava.data.auth.network.interceptor.AuthorizationInterceptor
import ru.internetcloud.strava.data.logout.network.api.LogoutApi
import ru.internetcloud.strava.data.profile.network.api.ProfileApi
import ru.internetcloud.strava.data.training.network.api.TrainingApi

object StravaApiFactory {

    private const val BASE_URL = "https://www.strava.com/"

    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    val trainingApi: TrainingApi
        get() = retrofit?.create() ?: error("retrofit is not initialized")

    val profileApi: ProfileApi
        get() = retrofit?.create() ?: error("retrofit is not initialized")

    val logoutApi: LogoutApi
        get() = retrofit?.create() ?: error("retrofit is not initialized")

    fun init(context: Context) {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .addInterceptor(AuthorizationFailedInterceptor(AuthorizationService(context), TokenStorage))
            .addInterceptor(
                HttpLoggingInterceptor {
                    // Timber.tag("rustam").d("-------")
                    // Timber.tag("rustam").d("HttpLoggingInterceptor BODY = $it")
                }
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient!!)
            .build()
    }
}
