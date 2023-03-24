package ru.internetcloud.strava

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.internetcloud.strava.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {

    interface OnBoardingEvents {
        fun onShowSecondFragment()
    }

    var hostActivity: OnBoardingEvents? = null

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding: FragmentOnBoardingBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hostActivity = context as OnBoardingEvents
    }

    override fun onDetach() {
        super.onDetach()
        hostActivity = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getStartedButton.setOnClickListener {
            hostActivity?.onShowSecondFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
