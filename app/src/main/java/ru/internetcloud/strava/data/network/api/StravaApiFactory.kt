package ru.internetcloud.strava.data.network.api

import android.content.Context
import android.util.Log
import net.openid.appauth.AuthorizationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.internetcloud.strava.presentation.auth.TokenStorage
import timber.log.Timber

object StravaApiFactory {

    private const val BASE_URL = "https://www.strava.com/api/v3/"

    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    val trainingApi: TrainingApi
        get() = retrofit?.create() ?: error("retrofit is not initialized")

    val profileApi: ProfileApi
        get() = retrofit?.create() ?: error("retrofit is not initialized")

    fun init(context: Context) {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor {
                    // Timber.tag("Network").d(it)
                }
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addNetworkInterceptor(AuthorizationInterceptor())
            .addNetworkInterceptor(AuthorizationFailedInterceptor(AuthorizationService(context), TokenStorage))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient!!)
            .build()
    }
}
