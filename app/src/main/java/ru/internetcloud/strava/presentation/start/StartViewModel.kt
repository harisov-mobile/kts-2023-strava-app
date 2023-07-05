package ru.internetcloud.strava.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.firstlaunch.usecase.GetFirstLaunchUseCase
import ru.internetcloud.strava.domain.firstlaunch.usecase.SetFirstLaunchUseCase
import ru.internetcloud.strava.domain.token.usecase.AuthUseCase

class StartViewModel(
    private val getFirstLaunchUseCase: GetFirstLaunchUseCase,
    private val setFirstLaunchUseCase: SetFirstLaunchUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val directionChannel = Channel<NavDirections>(Channel.BUFFERED)
    val directionFlow: Flow<NavDirections>
        get() = directionChannel.receiveAsFlow()

    fun getDirectionToNavigate() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                directionChannel.trySend(
                    if (getFirstLaunchUseCase.isFirstLaunch()) {
                        setFirstLaunchUseCase.setFirstLaunchToFalse()
                        StartFragmentDirections.actionStartFragmentToOnBoardingFragment()
                    } else {
                        if (authUseCase.isAuthorized()) {
                            StartFragmentDirections.actionStartFragmentToMainFragment()
                        } else {
                            StartFragmentDirections.actionStartFragmentToAuthFragment(
                                messageResId = R.string.auth_standart_message
                            )
                        }
                    }
                )
            }
        }
    }
}
