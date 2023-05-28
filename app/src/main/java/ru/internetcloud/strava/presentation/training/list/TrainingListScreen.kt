package ru.internetcloud.strava.presentation.training.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.ProfileWithTrainingList
import ru.internetcloud.strava.presentation.common.compose.ShowEmptyData
import ru.internetcloud.strava.presentation.common.compose.ShowError
import ru.internetcloud.strava.presentation.common.compose.ShowLoadingData
import ru.internetcloud.strava.presentation.common.compose.ShowSource
import ru.internetcloud.strava.presentation.common.compose.TopBarWithLogout
import ru.internetcloud.strava.presentation.util.UiState
import ru.internetcloud.strava.presentation.util.addLine

@Composable
fun ShowTrainingListScreen(
    onTrainingClickListener: (id: Long) -> Unit
) {
    val viewModel: TrainingListViewModel = viewModel()
    val screenState = viewModel.screenState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val currentState = screenState.value

    Scaffold(
        topBar = {
            TopBarWithLogout(title = stringResource(id = R.string.navigation_item_home))
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
                            viewModel.fetchStravaActivities()
                        }
                    )
                }
                UiState.Loading -> {
                    ShowLoadingData()
                }
                is UiState.Success -> {
                    ShowTrainings(
                        profileWithTrainings = currentState.data,
                        source = currentState.source,
                        onTrainingClickListener = onTrainingClickListener
                    )
                }
                UiState.EmptyData -> {
                    ShowEmptyData(message = stringResource(id = R.string.training_list_is_empty))
                }
            }
        }
    }
}

@Composable
private fun ShowTrainings(
    profileWithTrainings: ProfileWithTrainingList,
    source: Source,
    onTrainingClickListener: (id: Long) -> Unit
) {
    Column {
        ShowSource(source)
        LazyColumn(
            contentPadding = PaddingValues(
                top = 8.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 72.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = profileWithTrainings.trainingList,
                key = { it.id }
            ) { training ->
                TrainingItemView(
                    profile = profileWithTrainings.profile,
                    training = training,
                    onTrainingClickListener = {
                        onTrainingClickListener(training.id)
                    }
                )
            }
        }
    }
}
