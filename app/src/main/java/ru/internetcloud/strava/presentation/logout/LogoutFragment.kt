package ru.internetcloud.strava.presentation.logout

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.internetcloud.strava.R
import ru.internetcloud.strava.databinding.FragmentLogoutBinding
import ru.internetcloud.strava.presentation.util.launchAndCollectIn

class LogoutFragment : Fragment(R.layout.fragment_logout) {

    private val binding by viewBinding(FragmentLogoutBinding::bind)

    private val viewModel: LogoutViewModel by viewModels {
        LogoutViewModelFactory(requireActivity().application)
    }

    private val logoutResponse = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            viewModel.webLogoutComplete()
        } else {
            // логаут отменен
            // делаем complete тк github не редиректит после логаута и пользователь закрывает CCT
            //viewModel.webLogoutComplete()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun bindViewModel() {
        viewModel.logoutPageFlow.launchAndCollectIn(viewLifecycleOwner) {
            logoutResponse.launch(it)
        }

        viewModel.logoutCompletedFlow.launchAndCollectIn(viewLifecycleOwner) {
            //findNavController().resetNavGraph(R.navigation.nav_graph)
        }
    }
}
