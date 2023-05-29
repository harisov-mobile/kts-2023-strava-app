package ru.internetcloud.strava.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import ru.internetcloud.strava.presentation.common.compose.StravaTheme
import ru.internetcloud.strava.presentation.main.composable.MainScreen

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            StravaTheme {
                MainScreen(app = requireActivity().application)
            }
        }
    }
}
