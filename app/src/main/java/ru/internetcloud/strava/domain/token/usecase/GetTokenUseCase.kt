package ru.internetcloud.strava.domain.token.usecase

import ru.internetcloud.strava.domain.token.TokenRepository
import ru.internetcloud.strava.domain.token.model.TokensModel

class GetTokenUseCase(private val tokenRepository: TokenRepository) {

    fun getToken(): TokensModel {
        return tokenRepository.getToken()
    }
}
