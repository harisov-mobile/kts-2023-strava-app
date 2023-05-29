package ru.internetcloud.strava.presentation.training.detail

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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.util.TrainingConverter
import ru.internetcloud.strava.presentation.common.compose.ShowEmptyData
import ru.internetcloud.strava.presentation.common.compose.ShowError
import ru.internetcloud.strava.presentation.common.compose.ShowLoadingData
import ru.internetcloud.strava.presentation.training.list.TimeDistanceSpeed
import ru.internetcloud.strava.presentation.util.DateTimeConverter
import ru.internetcloud.strava.presentation.util.UiState
import ru.internetcloud.strava.presentation.util.addLine

@Composable
fun ShowTrainingDetailScreen(
    onBackPressed: () -> Unit,
    trainingId: Long
) {
    val viewModel: TrainingDetailViewModel = viewModel(
        factory = TrainingDetailViewModelFactory(id = trainingId)
    )
    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val currentState = screenState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.training_app_bar_title))
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
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
                        onTryAgainClick = {
                            viewModel.fetchTraining(id = trainingId)
                        }
                    )
                }
                UiState.Loading -> {
                    ShowLoadingData()
                }
                is UiState.Success -> {
                    ShowTraining(
                        profile = currentState.data.profile,
                        training = currentState.data.training
                    )
                }
                is UiState.EmptyData -> {
                    ShowEmptyData(message = stringResource(id = R.string.no_data))
                }
            }
        }
    }
}

@Composable
private fun ShowTraining(
    profile: Profile,
    training: Training
) {
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
    }
}
