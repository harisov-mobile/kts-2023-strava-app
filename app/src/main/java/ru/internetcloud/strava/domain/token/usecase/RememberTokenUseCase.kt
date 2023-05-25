package ru.internetcloud.strava.domain.token.usecase

import ru.internetcloud.strava.domain.token.TokenRepository
import ru.internetcloud.strava.domain.token.model.TokensModel

class RememberTokenUseCase(private val tokenRepository: TokenRepository) {

    fun rememberToken(tokensModel: TokensModel) {
        return tokenRepository.rememberToken(tokensModel)
    }
}
