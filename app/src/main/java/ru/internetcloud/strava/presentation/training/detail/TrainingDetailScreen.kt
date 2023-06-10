package ru.internetcloud.strava.presentation.training.detail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.profile.model.ProfileWithTraining
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.util.TrainingConverter
import ru.internetcloud.strava.presentation.common.compose.ShowEmptyData
import ru.internetcloud.strava.presentation.common.compose.ShowError
import ru.internetcloud.strava.presentation.common.compose.ShowLoadingData
import ru.internetcloud.strava.presentation.common.compose.ShowSource
import ru.internetcloud.strava.presentation.training.list.TimeDistanceSpeed
import ru.internetcloud.strava.presentation.util.DateTimeConverter
import ru.internetcloud.strava.presentation.util.UiState
import ru.internetcloud.strava.presentation.util.addLine
import ru.internetcloud.strava.presentation.util.parseStringVs

@Composable
fun TrainingDetailScreen(
    trainingId: Long,
    currentBackStackEntry: NavBackStackEntry?,
    refreshKey: String,
    onBackPressed: () -> Unit,
    onBackWithRefresh: () -> Unit,
    onEditTraining: (id: Long) -> Unit
) {
    val viewModel: TrainingDetailViewModel = viewModel(
        factory = TrainingDetailViewModelFactory(id = trainingId)
    )
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    val showDropdownMenu = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val shouldRefresh = currentBackStackEntry?.savedStateHandle?.remove<Boolean?>(refreshKey)
    shouldRefresh?.let { refresh ->
        if (refresh) {
            viewModel.fetchTraining(trainingId, isChanged = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.training_app_bar_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (screenState is UiState.Success
                                && (screenState as UiState.Success<ProfileWithTraining>).isChanged
                            ) {
                                onBackWithRefresh()
                            } else {
                                onBackPressed()
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
                    if (screenState is UiState.Success) {
                        IconButton(onClick = { showDropdownMenu.value = !showDropdownMenu.value }) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = showDropdownMenu.value,
                            onDismissRequest = { showDropdownMenu.value = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                onEditTraining(trainingId)
                            }) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = stringResource(id = R.string.menu_edit))
                            }

                            DropdownMenuItem(onClick = {
                                viewModel.deleteTraining()
                            }) {
                                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = stringResource(id = R.string.menu_delete))
                            }
                        }
                    }
                }
            )
        }
    ) { paddingContent ->
        Box(modifier = Modifier.padding(paddingContent)) {
            when (screenState) {
                is UiState.Error -> {
                    ShowError(
                        message = stringResource(id = R.string.strava_server_unavailable)
                            .addLine(
                                (screenState as UiState.Error).exception.message.toString()
                            ),
                        onTryAgainClick = {
                            viewModel.fetchTraining(id = trainingId, isChanged = false)
                        }
                    )
                }

                UiState.Loading -> {
                    ShowLoadingData()
                }

                is UiState.Success -> {
                    ShowTraining(
                        profile = (screenState as UiState.Success<ProfileWithTraining>).data.profile,
                        training = (screenState as UiState.Success<ProfileWithTraining>).data.training,
                        source = (screenState as UiState.Success<ProfileWithTraining>).source
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
                is TrainingDetailScreenEvent.NavigateBack -> {
                    onBackPressed()
                }

                is TrainingDetailScreenEvent.ShowMessage -> {
                    Toast.makeText(
                        context,
                        context.parseStringVs(event.stringVs),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    BackHandler {
        if (screenState is UiState.Success && (screenState as UiState.Success<ProfileWithTraining>).isChanged) {
            onBackWithRefresh()
        } else {
            onBackPressed()
        }
    }
}

@Composable
private fun ShowTraining(
    profile: Profile,
    training: Training,
    source: Source
) {
    Column {
        ShowSource(source)
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row {
                AsyncImage(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    model = profile.imageUrlMedium,
                    placeholder = painterResource(id = R.drawable.no_photo),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${profile.firstName} ${profile.lastName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = DateTimeConverter.getDateTimeStringWithGMT(training.startDate),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = training.name,
                fontWeight = FontWeight.Bold
            )
            if (training.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = training.description,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TimeDistanceSpeed(training = TrainingConverter.fromTrainingToTrainingListItem(training))

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = training.sportType,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
