package ru.internetcloud.strava.domain.token.usecase

import ru.internetcloud.strava.domain.token.TokenRepository

class AuthUseCase(private val tokenRepository: TokenRepository) {

    suspend fun isAuthorized(): Boolean {
        return tokenRepository.isAuthorized()
    }
}
