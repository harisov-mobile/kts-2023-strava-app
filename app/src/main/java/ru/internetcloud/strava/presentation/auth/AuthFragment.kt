package ru.internetcloud.strava.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import ru.internetcloud.strava.R
import ru.internetcloud.strava.databinding.FragmentAuthBinding
import ru.internetcloud.strava.presentation.util.launchAndCollectIn
import ru.internetcloud.strava.presentation.util.snackbar

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel: AuthViewModel by viewModels()
    private val binding by viewBinding(FragmentAuthBinding::bind)
    private val args by navArgs<AuthFragmentArgs>()

    private val getAuthResponse = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val dataIntent = it.data ?: return@registerForActivityResult
        handleAuthResponseIntent(dataIntent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()
        bindViewModel()
    }

    private fun updateUI() {
        args.message?.let { messageText ->
            binding.message.setText(messageText)
            binding.message.visibility = View.VISIBLE
        }
    }

    private fun bindViewModel() {
        binding.loginButton.setOnClickListener {
            viewModel.resetLocalCache()
            viewModel.openLoginPage()
        }

        viewModel.loadingFlow.launchAndCollectIn(viewLifecycleOwner) {
            updateIsLoading(it)
        }

        viewModel.openAuthPageFlow.launchAndCollectIn(viewLifecycleOwner) {
            openAuthPage(it)
        }

        viewModel.toastFlow.launchAndCollectIn(viewLifecycleOwner) {
            snackbar(binding.root, getString(it))
        }

        viewModel.authSuccessFlow.launchAndCollectIn(viewLifecycleOwner) {
            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToMainFragment())
        }
    }

    private fun updateIsLoading(isLoading: Boolean) = with(binding) {
        loginButton.isVisible = !isLoading
        loginProgress.isVisible = isLoading
    }

    private fun openAuthPage(intent: Intent) {
        getAuthResponse.launch(intent)
    }

    private fun handleAuthResponseIntent(intent: Intent) {
        // пытаемся получить ошибку из ответа. null - если все ок
        val exception = AuthorizationException.fromIntent(intent)

        // пытаемся получить запрос для обмена кода на токен, null - если произошла ошибка
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)?.createTokenExchangeRequest()

        when {
            // авторизация завершались ошибкой
            exception != null -> viewModel.onAuthCodeFailed(exception)

            // авторизация прошла успешно, меняем код на токен
            tokenExchangeRequest != null -> viewModel.onAuthCodeReceived(tokenExchangeRequest)
        }
    }
}
