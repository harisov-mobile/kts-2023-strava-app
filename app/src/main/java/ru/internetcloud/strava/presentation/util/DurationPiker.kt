package ru.internetcloud.strava.presentation.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.internetcloud.strava.R

private const val MAX_HOUR = 23
private const val MAX_MINUTE = 59
private const val MAX_SECOND = 59

@Composable
fun DurationPickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int) -> Unit,
    hour: Int,
    minute: Int,
    second: Int
) {
    if (show) {
        val hours = remember { (0..MAX_HOUR).map { it.toString() } }
        val hoursPickerState = rememberPickerState()
        val startIndexOfHours = hours.indexOf(hour.toString())

        val minutes = remember { (0..MAX_MINUTE).map { it.toString() } }
        val minutesPickerState = rememberPickerState()
        val startIndexOfMinutes = minutes.indexOf(minute.toString())

        val seconds = remember { (0..MAX_SECOND).map { it.toString() } }
        val secondsPickerState = rememberPickerState()
        val startIndexOfSeconds = seconds.indexOf(second.toString())

        Dialog(
            onDismissRequest = onDismiss,
            content = {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colors.surface,
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Row {
                                Spacer(modifier = Modifier.width(48.dp))
                                Text(
                                    text = stringResource(id = R.string.duration_hour),
                                    modifier = Modifier.weight(0.3f)
                                )
                                Text(
                                    text = stringResource(id = R.string.duration_minute),
                                    modifier = Modifier.weight(0.3f)
                                )
                                Text(
                                    text = stringResource(id = R.string.duration_second),
                                    modifier = Modifier.weight(0.2f)
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                CustomPicker(
                                    state = hoursPickerState,
                                    items = hours,
                                    startIndex = startIndexOfHours,
                                    visibleItemsCount = 5,
                                    modifier = Modifier.weight(0.3f),
                                    textModifier = Modifier.padding(8.dp),
                                    textStyle = TextStyle(fontSize = 32.sp)
                                )
                                CustomPicker(
                                    state = minutesPickerState,
                                    items = minutes,
                                    startIndex = startIndexOfMinutes,
                                    visibleItemsCount = 5,
                                    modifier = Modifier.weight(0.3f),
                                    textModifier = Modifier.padding(8.dp),
                                    textStyle = TextStyle(fontSize = 32.sp)
                                )
                                CustomPicker(
                                    state = secondsPickerState,
                                    items = seconds,
                                    startIndex = startIndexOfSeconds,
                                    visibleItemsCount = 5,
                                    modifier = Modifier.weight(0.3f),
                                    textModifier = Modifier.padding(8.dp),
                                    textStyle = TextStyle(fontSize = 32.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                onClick = remember {
                                    {
                                        onConfirm(
                                            hoursPickerState.selectedItem.toInt(),
                                            minutesPickerState.selectedItem.toInt(),
                                            secondsPickerState.selectedItem.toInt()
                                        )
                                        onDismiss()
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.duration_confirm_button)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
