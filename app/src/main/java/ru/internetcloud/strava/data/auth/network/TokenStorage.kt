package ru.internetcloud.strava.data.auth.network

import ru.internetcloud.strava.data.token.TokenSharedPreferencesStorage
import ru.internetcloud.strava.domain.token.model.TokensModel

object TokenStorage {
    var accessToken: String? = null
    var refreshToken: String? = null
    var idToken: String? = null

    fun readTokenFromPrefs() {
        val token = TokenSharedPreferencesStorage.getTokenData()
        accessToken = token.accessToken
        refreshToken = token.refreshToken
        idToken = token.idToken
    }

    fun saveTokenToPrefs() {
        TokenSharedPreferencesStorage.saveTokenData(
            TokensModel(
                accessToken = this.accessToken,
                refreshToken = this.refreshToken,
                idToken = this.idToken
            )
        )
    }
}
