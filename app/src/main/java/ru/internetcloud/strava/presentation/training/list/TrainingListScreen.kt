package ru.internetcloud.strava.presentation.training.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import org.koin.androidx.compose.viewModel
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.training.model.TrainingListItem
import ru.internetcloud.strava.presentation.common.compose.ListProgressIndicator
import ru.internetcloud.strava.presentation.common.compose.ShowError
import ru.internetcloud.strava.presentation.common.compose.ShowSource
import ru.internetcloud.strava.presentation.common.compose.TopBarWithLogout
import ru.internetcloud.strava.presentation.common.list.SwipeRefresh
import ru.internetcloud.strava.presentation.common.list.isRefreshLoading
import ru.internetcloud.strava.presentation.common.list.isSwipeEnabled
import ru.internetcloud.strava.presentation.common.list.itemsWithListStates
import ru.internetcloud.strava.presentation.common.list.loadNextPage
import ru.internetcloud.strava.presentation.training.list.model.trainingListItemsWithState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrainingListScreen(
    currentBackStackEntry: NavBackStackEntry?,
    refreshKey: String,
    onTrainingClick: (id: Long) -> Unit,
    onFloatingActionButtonClick: () -> Unit
) {
    val viewModel: TrainingListViewModel by viewModel()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val key: ((item: TrainingListItem) -> Any) = remember { { item -> item.id } }

    val shouldRefresh = currentBackStackEntry?.savedStateHandle?.remove<Boolean?>(refreshKey)
    shouldRefresh?.let { refresh ->
        if (refresh) {
            viewModel.onReboot()
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
                onClick = remember { { onFloatingActionButtonClick() } }
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            pullRefreshState = rememberPullRefreshState(
                screenState.isRefreshLoading(),
                onRefresh = viewModel::onRefresh
            ),
            swipeEnabled = screenState.isSwipeEnabled(),
            refreshing = screenState.isRefreshLoading()
        ) {
            Column {
                ShowSource(screenState.source)
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 72.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsWithListStates(
                        items = screenState.trainingListItemsWithState(),
                        key = key,
                        onErrorRetry = viewModel::onReboot,
                        onErrorRetryPage = viewModel::onRetry,
                        onEmptyRetry = viewModel::onReboot,
                        loadListContent = {
                            ListProgressIndicator()
                        }
                    ) { index, training ->
                        screenState.profile?.let { profile ->
                            TrainingItemView(
                                profile = profile,
                                training = training,
                                onTrainingClickListener = remember {
                                    {
                                        onTrainingClick(training.id)
                                    }
                                }
                            )

                            screenState.loadNextPage(
                                index = index,
                                onLoadNextPage = viewModel::onLoadNextPage
                            )
                        } ?: ShowError(
                            message = stringResource(id = R.string.strava_server_unavailable),
                            onTryAgainClick = {
                                viewModel.onReboot()
                            }
                        )
                    }
                }
            }
        }
    }
}
