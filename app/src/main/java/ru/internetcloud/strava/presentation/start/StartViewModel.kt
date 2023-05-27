package ru.internetcloud.strava.presentation.start

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import ru.internetcloud.strava.R
import ru.internetcloud.strava.data.onboarding.repository.OnboardingRepositoryImpl
import ru.internetcloud.strava.data.token.repository.TokenRepositoryImpl
import ru.internetcloud.strava.domain.onboarding.usecase.GetOnboardingParamsUseCase
import ru.internetcloud.strava.domain.onboarding.usecase.SaveOnboardingParamsUseCase
import ru.internetcloud.strava.domain.token.usecase.GetTokenUseCase
import ru.internetcloud.strava.domain.token.usecase.RememberTokenUseCase

class StartViewModel(private val app: Application) : ViewModel() {

    private val onboardingRepository = OnboardingRepositoryImpl()
    private val getOnboardingParamsUseCase = GetOnboardingParamsUseCase(onboardingRepository)
    private val saveOnboardingParamsUseCase = SaveOnboardingParamsUseCase(onboardingRepository)

    private val tokenRepository = TokenRepositoryImpl()
    private val getTokenUseCase = GetTokenUseCase(tokenRepository)
    private val rememberTokenUseCase = RememberTokenUseCase(tokenRepository)

    fun getDirectionToNavigate(): NavDirections {
        val onboardingParams = getOnboardingParamsUseCase.getOnboardingParams()

        return if (onboardingParams.isFirstLaunch) {
            saveOnboardingParamsUseCase.saveOnboardingParams()
            StartFragmentDirections.actionStartFragmentToOnBoardingFragment()
        } else {
            val token = getTokenUseCase.getToken()

            if (token.accessToken.isNullOrEmpty()) {
                StartFragmentDirections.actionStartFragmentToAuthFragment(
                    message = app.getString(R.string.auth_standart_message)
                )
            } else {
                rememberTokenUseCase.rememberToken(token)
                StartFragmentDirections.actionStartFragmentToMainFragment()
            }
        }
    }
}
