package ru.internetcloud.strava.presentation.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.internetcloud.strava.R
import ru.internetcloud.strava.databinding.FragmentLoginBinding
import timber.log.Timber

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
        // doOnTextChanged нужно навешивать здесь, а не в onCreateView или onViewCreated, т.к. там еще не восстановлено
        // EditText и слушатели будут "дергаться" лишний раз когда ОС Андроид сама восстановит состояние EditText
        setupOnTextChangedListeners()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { currentState ->
            // чтобы "doOnTextChanged" не дергались и не зацикливалось приложение
            if (!currentState.email.equals(binding.emailEditText.text.toString())) {
                binding.emailEditText.setText(currentState.email)
            }
            if (!currentState.password.equals(binding.passwordEditText.text.toString())) {
                binding.passwordEditText.setText(currentState.password)
            }
            binding.loginButton.isEnabled = currentState.loginEnabled
        }
    }

    private fun setupOnTextChangedListeners() {
        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setEmail(text.toString())
        }
        binding.passwordEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setPassword(text.toString())
        }
    }
}
