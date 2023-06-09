package ru.internetcloud.strava.data.token

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import ru.internetcloud.strava.domain.token.model.TokensModel

class TokenSharedPreferencesStorage(
    applicaton: Application
) {
    private var tokenSharedPrefs: SharedPreferences =
        applicaton.getSharedPreferences(TOKEN_SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    suspend fun getTokenData(): TokensModel {
        return TokensModel(
            accessToken = tokenSharedPrefs.getString(KEY_ACCESS_TOKEN, null),
            refreshToken = tokenSharedPrefs.getString(KEY_REFRESH_TOKEN, null),
            idToken = tokenSharedPrefs.getString(KEY_ID_TOKEN, null)
        )
    }

    suspend fun saveTokenData(tokensModel: TokensModel) {
        tokenSharedPrefs.edit().putString(KEY_ACCESS_TOKEN, tokensModel.accessToken).apply()
        tokenSharedPrefs.edit().putString(KEY_REFRESH_TOKEN, tokensModel.refreshToken).apply()
        tokenSharedPrefs.edit().putString(KEY_ID_TOKEN, tokensModel.idToken).apply()
    }

    suspend fun clearTokenData() {
        tokenSharedPrefs.edit().putString(KEY_ACCESS_TOKEN, null).apply()
        tokenSharedPrefs.edit().putString(KEY_REFRESH_TOKEN, null).apply()
        tokenSharedPrefs.edit().putString(KEY_ID_TOKEN, null).apply()
    }

    suspend fun isAuthorized(): Boolean {
        return !getTokenData().accessToken.isNullOrEmpty()
    }

    companion object {
        private const val TOKEN_SHARED_PREFS_NAME = "token_shared_prefs"
        private const val KEY_ACCESS_TOKEN = "key_access_token"
        private const val KEY_REFRESH_TOKEN = "key_refresh_token"
        private const val KEY_ID_TOKEN = "key_id_token"
    }
}
