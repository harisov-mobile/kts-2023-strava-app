package ru.internetcloud.strava.di

import net.openid.appauth.AuthorizationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.internetcloud.strava.data.auth.network.interceptor.AuthorizationFailedInterceptor
import ru.internetcloud.strava.data.auth.network.interceptor.AuthorizationInterceptor
import ru.internetcloud.strava.data.logout.network.api.LogoutApi
import ru.internetcloud.strava.data.profile.network.api.ProfileApi
import ru.internetcloud.strava.data.training.network.api.TrainingApi
import ru.internetcloud.strava.domain.token.TokenRepository

val networkModule = module {

    val BASE_URL = "https://www.strava.com/"

    fun provideHttpClient(
        authorizationService: AuthorizationService,
        tokenRepository: TokenRepository
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(tokenRepository = tokenRepository))
            .addInterceptor(
                AuthorizationFailedInterceptor(
                    authorizationService = authorizationService,
                    tokenRepository = tokenRepository
                )
            )

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor {
                // Timber.tag("rustam").d("-------")
                // Timber.tag("rustam").d("HttpLoggingInterceptor BODY = $it")
            }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        okHttpClientBuilder.build()

        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create()
    }

    fun provideTrainingApi(retrofit: Retrofit): TrainingApi {
        return retrofit.create()
    }

    fun provideLogoutApi(retrofit: Retrofit): LogoutApi {
        return retrofit.create()
    }

    single {
        provideHttpClient(
            authorizationService = get(),
            tokenRepository = get()
        )
    }

    single {
        provideRetrofit(okHttpClient = get(), baseUrl = BASE_URL)
    }

    single {
        provideProfileApi(retrofit = get())
    }

    single {
        provideTrainingApi(retrofit = get())
    }

    single {
        provideLogoutApi(retrofit = get())
    }
}
