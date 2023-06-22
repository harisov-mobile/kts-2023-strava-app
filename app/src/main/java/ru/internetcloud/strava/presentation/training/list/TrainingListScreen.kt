package ru.internetcloud.strava.presentation.training.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowTrainingListScreen(
    currentBackStackEntry: NavBackStackEntry?,
    refreshKey: String,
    onTrainingClickListener: (id: Long) -> Unit,
    onFABClickListener: () -> Unit
) {
    val viewModel: TrainingListViewModel = viewModel()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val state = screenState

    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.fetchTrainings() })

    val shouldRefresh = currentBackStackEntry?.savedStateHandle?.remove<Boolean?>(refreshKey)
    shouldRefresh?.let { refresh ->
        if (refresh) {
            viewModel.fetchTrainings()
        }
    }

    Scaffold(
        topBar = {
            TopBarWithLogout(title = stringResource(id = R.string.navigation_item_home))
        },
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                onClick = remember { { onFABClickListener() } }
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
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
                        onTryAgainClick = {
                            viewModel.fetchTrainings()
                        }
                    )
                }

                UiState.Loading -> {
                    ShowLoadingData()
                }

                is UiState.Success -> {
                    ShowTrainings(
                        profileWithTrainings = state.data,
                        source = state.source,
                        onTrainingClickListener = onTrainingClickListener,
                        isRefreshing = isRefreshing,
                        pullRefreshState = pullRefreshState
                    )
                }

                UiState.EmptyData -> {
                    ShowEmptyData(message = stringResource(id = R.string.training_list_is_empty))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ShowTrainings(
    profileWithTrainings: ProfileWithTrainingList,
    source: Source,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    onTrainingClickListener: (id: Long) -> Unit
) {
    Box(
        Modifier
            .pullRefresh(pullRefreshState)
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
                        onTrainingClickListener = remember {
                            {
                                onTrainingClickListener(training.id)
                            }
                        }
                    )
                }
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(
                Alignment.TopCenter
            )
        )
    }
}
