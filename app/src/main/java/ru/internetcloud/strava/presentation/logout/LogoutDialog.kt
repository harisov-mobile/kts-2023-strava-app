package ru.internetcloud.strava.presentation.logout

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.internetcloud.strava.R

@Composable
fun LogoutDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = stringResource(id = R.string.logout_confirm_button)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(id = R.string.logout_dismiss_button)) }
            },
            title = null,
            text = { Text(text = stringResource(id = R.string.logout_confirmation_question)) }
        )
    }
}
