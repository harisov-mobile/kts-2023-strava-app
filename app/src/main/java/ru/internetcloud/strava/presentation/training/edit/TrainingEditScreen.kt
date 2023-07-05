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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.Calendar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.getSportByName
import ru.internetcloud.strava.domain.common.util.DateConverter
import ru.internetcloud.strava.domain.common.util.toFloatOrDefault
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.presentation.common.compose.ShowEmptyData
import ru.internetcloud.strava.presentation.common.compose.ShowError
import ru.internetcloud.strava.presentation.common.compose.ShowLoadingData
import ru.internetcloud.strava.presentation.training.edit.composable.SportPicker
import ru.internetcloud.strava.presentation.util.DurationPickerDialog
import ru.internetcloud.strava.presentation.util.Formatter
import ru.internetcloud.strava.presentation.util.UiState
import ru.internetcloud.strava.presentation.util.addLine

@Composable
fun TrainingEditScreen(
    trainingId: Long,
    editMode: EditMode,
    onReturn: (id: Long) -> Unit,
    onBackPressed: () -> Unit,
    onBackWithRefresh: () -> Unit
) {
    val context = LocalContext.current

    val exitHereDialogShownState = rememberSaveable { mutableStateOf(false) }

    val viewModel: TrainingEditViewModel by viewModel {
        parametersOf(trainingId, editMode)
    }
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val state = screenState

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
                        onClick = remember(screenState) {
                            {
                                if (screenState is UiState.Success) {
                                    exitHereDialogShownState.value = true
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
                    if (state is UiState.Success) {
                        if (state.saving) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colors.onPrimary,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.training_edit_save_button),
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    if (!state.saving) {
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
            when (state) {
                is UiState.Error -> {
                    ShowError(
                        message = stringResource(id = R.string.strava_server_unavailable)
                            .addLine(
                                state.exception.message.toString()
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
                        training = state.data,
                        onEvent = viewModel::handleEvent,
                        isChanged = state.isChanged,
                        shouldExitHere = (exitHereDialogShownState.value),
                        exitHere = {
                            exitHereDialogShownState.value = true
                        },
                        stayHere = {
                            exitHereDialogShownState.value = false
                        },
                        onBackPressed = onBackPressed
                    )
                }

                is UiState.EmptyData -> {
                    ShowEmptyData(
                        message = stringResource(id = R.string.no_data),
                        onRefreshClick = remember {
                            {
                                viewModel.fetchTraining(id = trainingId)
                            }
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.screenEventFlow.collect { event ->
            when (event) {
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

@OptIn(ExperimentalMaterialApi::class)
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
    val dateConverter: DateConverter by inject()
    val showDurationDialog = remember { mutableStateOf(false) }
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

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val gesturesEnabled = rememberSaveable { mutableStateOf(false) }
    val sportBottomDrawerState: BottomDrawerState =
        rememberBottomDrawerState(BottomDrawerValue.Closed, confirmStateChange = {
            if (it == BottomDrawerValue.Closed) {
                coroutineScope.launch {
                    delay(200)
                    gesturesEnabled.value = false
                }
            }
            true
        })

    BottomDrawer(
        drawerState = sportBottomDrawerState,
        gesturesEnabled = gesturesEnabled.value,
        drawerContent = {
            SportPicker(
                currentSport = getSportByName(training.sport),
                onSportSelect = { sport ->
                    coroutineScope.launch {
                        gesturesEnabled.value = false
                        sportBottomDrawerState.close()
                    }
                    onEvent(EditTrainingEvent.OnSportTypeChange(sport.toString()))
                }
            )
        },
        content = {
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
                    value = stringResource(id = getSportByName(training.sport).label),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.training_edit_field_sport)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                coroutineScope.launch {
                                    gesturesEnabled.value = true
                                    sportBottomDrawerState.open()
                                }
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = stringResource(id = R.string.training_edit_chapter_activity_stats))

                Spacer(modifier = Modifier.height(16.dp))

                // Start Date
                OutlinedTextField(
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = null)
                    },
                    readOnly = true,
                    value = dateConverter.getDateString(training.startDate),
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
                    value = dateConverter.getTimeString(training.startDate),
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
        }
    )

    BackHandler {
        if (isChanged) {
            exitHere() // выйти с экрана или показать AlertDialog с вопросом
        } else {
            // ничего не менялось, выйти без сохранения на предыдущий экран:
            onBackPressed()
        }
    }
}
