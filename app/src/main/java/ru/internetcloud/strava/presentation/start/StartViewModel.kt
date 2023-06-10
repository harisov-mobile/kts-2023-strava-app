package ru.internetcloud.strava.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.R
import ru.internetcloud.strava.data.firstlaunch.repository.FirstLaunchRepositoryImpl
import ru.internetcloud.strava.data.token.repository.TokenRepositoryImpl
import ru.internetcloud.strava.domain.firstlaunch.usecase.GetFirstLaunchUseCase
import ru.internetcloud.strava.domain.firstlaunch.usecase.SetFirstLaunchUseCase
import ru.internetcloud.strava.domain.token.usecase.AuthUseCase

class StartViewModel : ViewModel() {

    private val firstLaunchRepository = FirstLaunchRepositoryImpl()
    private val getFirstLaunchUseCase = GetFirstLaunchUseCase(firstLaunchRepository)
    private val setFirstLaunchUseCase = SetFirstLaunchUseCase(firstLaunchRepository)

    private val tokenRepository = TokenRepositoryImpl()
    private val authUseCase = AuthUseCase(tokenRepository)

    private val directionChannel = Channel<NavDirections>(Channel.BUFFERED)
    val directionFlow: Flow<NavDirections>
        get() = directionChannel.receiveAsFlow()

    fun getDirectionToNavigate() {
        viewModelScope.launch {
            directionChannel.trySend(
                if (getFirstLaunchUseCase.isFirstLaunch()) {
                    setFirstLaunchUseCase.setFirstLaunchToFalse()
                    StartFragmentDirections.actionStartFragmentToOnBoardingFragment()
                } else {
                    if (authUseCase.isAuthorized()) {
                        StartFragmentDirections.actionStartFragmentToAuthFragment(
                            messageResId = R.string.auth_standart_message
                        )
                    } else {
                        StartFragmentDirections.actionStartFragmentToMainFragment()
                    }
                }
            )
        }
    }
}
