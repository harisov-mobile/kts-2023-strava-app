package ru.internetcloud.strava.presentation.training.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.SportTypeKeeper
import ru.internetcloud.strava.domain.common.util.DateConverter
import ru.internetcloud.strava.domain.common.util.toFloatOrDefault
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.presentation.common.compose.ShowEmptyData
import ru.internetcloud.strava.presentation.common.compose.ShowError
import ru.internetcloud.strava.presentation.common.compose.ShowLoadingData
import ru.internetcloud.strava.presentation.util.DurationPickerDialog
import ru.internetcloud.strava.presentation.util.Formatter
import ru.internetcloud.strava.presentation.util.UiState
import ru.internetcloud.strava.presentation.util.addLine

@Composable
fun ShowTrainingEditScreen(
    trainingId: Long,
    editMode: EditMode,
    onReturn: (id: Long) -> Unit,
    onBackPressed: () -> Unit,
    onBackWithRefresh: () -> Unit
) {
    val context = LocalContext.current

    val shouldExitHere = remember { mutableStateOf(false) }

    val viewModel: TrainingEditViewModel = viewModel(
        factory = TrainingEditViewModelFactory(id = trainingId, editMode = editMode)
    )
    val screenState = viewModel.screenState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val currentState = screenState.value

    val topAppBarTitle = when (editMode) {
        EditMode.Add -> stringResource(id = R.string.training_add_app_bar_title)
        EditMode.Edit -> stringResource(id = R.string.training_edit_app_bar_title)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = topAppBarTitle)
                },
                navigationIcon = {
                    IconButton(
                        onClick = remember(currentState) {
                            {
                                if (currentState is UiState.Success) {
                                    shouldExitHere.value = true
                                } else {
                                    when (editMode) {
                                        EditMode.Add -> onBackPressed()
                                        EditMode.Edit -> onReturn(trainingId)
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (currentState is UiState.Success) {
                        if (currentState.saving) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colors.surface,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.training_edit_save_button),
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.surface,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    if (!currentState.saving) {
                                        viewModel.saveTraining()
                                    }
                                }
                        )
                    }
                }
            )
        }
    ) { paddingContent ->
        Box(modifier = Modifier.padding(paddingContent)) {
            when (currentState) {
                is UiState.Error -> {
                    ShowError(
                        message = stringResource(id = R.string.strava_server_unavailable)
                            .addLine(
                                currentState.exception.message.toString()
                            ),
                        onTryAgainClick = remember {
                            {
                                viewModel.fetchTraining(id = trainingId)
                            }
                        }
                    )
                }

                UiState.Loading -> {
                    ShowLoadingData()
                }

                is UiState.Success -> {
                    ShowTrainingEdit(
                        training = currentState.data,
                        onEvent = viewModel::handleEvent,
                        isChanged = currentState.isChanged,
                        shouldExitHere = (shouldExitHere.value),
                        exitHere = {
                            shouldExitHere.value = true
                        },
                        stayHere = {
                            shouldExitHere.value = false
                        },
                        onBackPressed = onBackPressed
                    )
                }

                is UiState.EmptyData -> {
                    ShowEmptyData(message = stringResource(id = R.string.no_data))
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.screenEventFlow.collect { event ->
            when (event) {
                is TrainingEditScreenEvent.NavigateToTrainingDetail -> {
                    onReturn(event.id)
                }

                is TrainingEditScreenEvent.NavigateBackWithRefresh -> {
                    onBackWithRefresh()
                }

                is TrainingEditScreenEvent.ShowMessage -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
private fun ShowTrainingEdit(
    training: Training,
    isChanged: Boolean,
    shouldExitHere: Boolean,
    onEvent: (EditTrainingEvent) -> Unit,
    exitHere: () -> Unit,
    stayHere: () -> Unit,
    onBackPressed: () -> Unit
) {
    val showDurationDialog = remember { mutableStateOf(false) }

    val expandedSportType = remember { mutableStateOf(false) }
    val suggestions = remember { SportTypeKeeper.getSportTypes() }
    val textfieldSize = remember { mutableStateOf(Size.Zero) }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    calendar.time = training.startDate
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val day = calendar[Calendar.DAY_OF_MONTH]
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, newYear: Int, newMonth: Int, newDay: Int ->
            onEvent(EditTrainingEvent.OnStartDateChange(year = newYear, month = newMonth, day = newDay))
        },
        year,
        month,
        day
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, newHour: Int, newMinute: Int ->
            onEvent(EditTrainingEvent.OnStartTimeChange(hour = newHour, minute = newMinute))
        },
        hour,
        minute,
        true
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxSize()
            .verticalScroll(
                state = scrollState
            )
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = training.name,
            onValueChange = remember { { onEvent(EditTrainingEvent.OnNameChange(it)) } },
            label = { Text(text = stringResource(id = R.string.training_edit_field_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = training.description,
            onValueChange = remember { { onEvent(EditTrainingEvent.OnDescriptionChange(it)) } },
            label = { Text(text = stringResource(id = R.string.training_edit_field_description)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sport Type
        OutlinedTextField(
            readOnly = true,
            value = training.sportType,
            onValueChange = remember { { onEvent(EditTrainingEvent.OnSportTypeChange(it)) } },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to the DropDown the same width
                    textfieldSize.value = coordinates.size.toSize()
                },
            label = { Text(text = stringResource(id = R.string.training_edit_field_sport_type)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    Modifier.clickable { expandedSportType.value = !expandedSportType.value }
                )
            }
        )

        DropdownMenu(
            expanded = expandedSportType.value,
            onDismissRequest = { expandedSportType.value = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.value.width.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        expandedSportType.value = false
                        onEvent(EditTrainingEvent.OnSportTypeChange(label))
                    }
                ) {
                    Text(text = label)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(id = R.string.training_edit_chapter_activity_stats))

        Spacer(modifier = Modifier.height(16.dp))

        // Start Date
        OutlinedTextField(
            leadingIcon = {
                Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = null)
            },
            readOnly = true,
            value = DateConverter.getDateString(training.startDate),
            onValueChange = remember { { } },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.training_edit_field_date)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = remember { { datePickerDialog.show() } }
                    )
                )
            }
        )

        // Start Time
        OutlinedTextField(
            leadingIcon = {
                Icon(imageVector = Icons.Filled.AccessTime, contentDescription = null)
            },
            readOnly = true,
            value = DateConverter.getTimeString(training.startDate),
            onValueChange = remember { { } },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.training_edit_field_start_time)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = remember { { timePickerDialog.show() } }
                    )
                )
            }
        )

        // Duration
        DurationPickerDialog(
            show = showDurationDialog.value,
            onDismiss = {
                showDurationDialog.value = false
            },
            onConfirm = remember {
                { hours, minutes, seconds ->
                    onEvent(
                        EditTrainingEvent.OnDurationChange(
                            durationTime = Formatter.getTimeInSeconds(
                                hours,
                                minutes,
                                seconds
                            )
                        )
                    )
                }
            },
            hour = Formatter.getHours(training.elapsedTime),
            minute = Formatter.getMinutes(training.elapsedTime),
            second = Formatter.getSeconds(training.elapsedTime)
        )

        OutlinedTextField(
            leadingIcon = {
                Icon(imageVector = Icons.Filled.HourglassEmpty, contentDescription = null)
            },
            readOnly = true,
            value = Formatter.getDetailedTime(
                training.elapsedTime,
                stringResource(id = R.string.training_hours_minutes),
                stringResource(id = R.string.training_minutes_seconds)
            ),
            onValueChange = remember { { } },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.training_edit_field_duration)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = remember {
                            {
                                showDurationDialog.value = true
                            }
                        }
                    )
                )
            }
        )

        // Distance
        OutlinedTextField(
            value = training.distance.toInt().toString(),
            onValueChange = remember { { onEvent(EditTrainingEvent.OnDistanceChange(it.toFloatOrDefault())) } },
            label = { Text(text = stringResource(id = R.string.training_edit_field_distance)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        // Button - Discard changes
        Button(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = exitHere // выйти с экрана или показать AlertDialog с вопросом
        ) {
            Text(
                text = stringResource(id = R.string.training_edit_discard_button)
            )
        }
    }

    if (shouldExitHere) {
        if (isChanged) {
            AlertDialog(
                onDismissRequest = stayHere,
                confirmButton = {
                    TextButton(onClick = {
                        // ничего не менялось, выйти без сохранения на предыдущий экран:
                        onBackPressed()
                    }) {
                        Text(text = stringResource(id = R.string.training_edit_discard_dialog_confirm_button))
                    }
                },
                dismissButton = {
                    TextButton(onClick = stayHere) {
                        Text(text = stringResource(id = R.string.training_edit_discard_dialog_dismiss_button))
                    }
                },
                title = null,
                text = { Text(text = stringResource(id = R.string.training_edit_discard_question)) }
            )
        } else {
            // ничего не менялось, выйти без сохранения на предыдущий экран:
            onBackPressed()
        }
    }

    BackHandler {
        if (isChanged) {
            exitHere() // выйти с экрана или показать AlertDialog с вопросом
        } else {
            // ничего не менялось, выйти без сохранения на предыдущий экран:
            onBackPressed()
        }
    }
}
