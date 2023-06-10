package ru.internetcloud.strava.presentation.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.util.launchAndCollectIn

class StartFragment : Fragment(R.layout.fragment_start) {

    private val startViewModel: StartViewModel by viewModels()
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        startViewModel.getDirectionToNavigate()
    }

    private fun observeViewModel() {
        startViewModel.directionFlow.launchAndCollectIn(viewLifecycleOwner) { direction ->
            scope.launch {
                delay(2000) // чтобы экран-заставку увидел пользователь
                findNavController().navigate(direction)
            }
        }
    }
}
