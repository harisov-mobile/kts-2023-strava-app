package ru.internetcloud.strava.presentation.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snackbar(view: View, message: String, isLengthShort: Boolean = true) {
    val length = if (isLengthShort) {
        Snackbar.LENGTH_SHORT
    } else {
        Snackbar.LENGTH_LONG
    }
    Snackbar.make(view, message, length).show()
}

fun Fragment.closeKeyboard() {
    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}
