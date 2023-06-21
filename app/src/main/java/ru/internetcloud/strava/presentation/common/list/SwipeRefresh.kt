package ru.internetcloud.strava.presentation.common.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeRefresh(
    refreshing: Boolean,
    modifier: Modifier = Modifier,
    swipeEnabled: Boolean = true,
    pullRefreshState: PullRefreshState,
    pullRefreshIndicator: @Composable BoxScope.() -> Unit = {
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    },
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier.pullRefresh(
            state = pullRefreshState,
            enabled = swipeEnabled
        )
    ) {
        content()
        pullRefreshIndicator()
    }
}
