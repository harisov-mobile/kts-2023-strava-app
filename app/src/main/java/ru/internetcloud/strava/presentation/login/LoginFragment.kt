package ru.internetcloud.strava.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import ru.internetcloud.strava.R
import ru.internetcloud.strava.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

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
        var message = ""
        binding.emailTextInputLayout.error = if (state.incorrectFields.isEmailIncorrect) {
            val temp = getString(R.string.email_incorrect)
            if (message.isBlank()) {
                message = temp
            } else {
                message = message + "\n" + temp
            }
            temp
        } else {
            null
        }

        binding.passwordTextInputLayout.error = if (state.incorrectFields.isPasswordIncorrect) {
            val temp = String.format(
                getString(R.string.password_incorrect),
                state.incorrectFields.validPasswordLengh.toString()
            )
            if (message.isBlank()) {
                message = temp
            } else {
                message = message + "\n" + temp
            }
            temp
        } else {
            null
        }

        if (state.incorrectFields.showErrorsInSnackbar &&
            (state.incorrectFields.isEmailIncorrect || state.incorrectFields.isPasswordIncorrect)
        ) {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun handleError(state: LoginState.Error) {
        Snackbar.make(binding.root, getString(state.stringResId), Snackbar.LENGTH_LONG).show()
    }

    private fun goToMainScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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

    private fun closeKeyboard() {
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        }
    }
}
