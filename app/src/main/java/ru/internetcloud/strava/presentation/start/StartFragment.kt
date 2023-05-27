package ru.internetcloud.strava.presentation.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class StartFragment : Fragment() {

    private val startViewModel: StartViewModel by viewModels {
        StartViewModelFactory(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findNavController().navigate(startViewModel.getDirectionToNavigate())
    }
}
