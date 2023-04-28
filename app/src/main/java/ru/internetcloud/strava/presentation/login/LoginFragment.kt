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
import ru.internetcloud.strava.presentation.util.closeKeyboard
import ru.internetcloud.strava.presentation.util.snackbar

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(requireActivity().application)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        sendFieldsToValidation()
        // doOnTextChanged нужно навешивать здесь, а не в onCreateView или onViewCreated, т.к. там еще не восстановлено
        // EditText и слушатели будут "дергаться" лишний раз когда ОС Андроид сама восстановит состояние EditText
        setupOnTextChangedListeners()
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            closeKeyboard()
            viewModel.login(
                email = binding.emailEditText.text.toString(),
                password = binding.passwordEditText.text.toString()
            )
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { currentState ->
            when (currentState) {
                is LoginState.InitialState -> {}
                is LoginState.NotValid -> handleNotValidState(currentState)
                is LoginState.IsValid -> handleIsValidState()
                is LoginState.Success -> goToMainScreen()
                is LoginState.Error -> handleError(currentState)
            }
        }
    }

    private fun handleIsValidState() {
        binding.emailTextInputLayout.error = null
        binding.passwordTextInputLayout.error = null
    }

    private fun handleNotValidState(state: LoginState.NotValid) {
        binding.emailTextInputLayout.error = state.incorrectFields.incorrectEmailMessage
        binding.passwordTextInputLayout.error = state.incorrectFields.incorrectPasswordMessage

        if (state.incorrectFields.showErrorsInSnackbar &&
            (state.incorrectFields.isEmailIncorrect || state.incorrectFields.isPasswordIncorrect)
        ) {
            snackbar(view = binding.root, message = state.incorrectFields.errorsMessage, isLengthShort = false)
        }
    }

    private fun handleError(state: LoginState.Error) {
        snackbar(view = binding.root, message = getString(state.stringResId), isLengthShort = false)
    }

    private fun goToMainScreen() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
    }

    private fun setupOnTextChangedListeners() {
        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            sendFieldsToValidation()
        }
        binding.passwordEditText.doOnTextChanged { text, _, _, _ ->
            sendFieldsToValidation()
        }
    }

    private fun sendFieldsToValidation() {
        viewModel.checkValidationAccordingLoginState(
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString()
        )
    }
}
