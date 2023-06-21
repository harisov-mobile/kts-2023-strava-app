package ru.internetcloud.strava.presentation.common.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.util.addLine

@Composable
fun ItemLoadingPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorLoadingDataBox(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier.fillMaxSize(),
    boxPaddingValues: PaddingValues = PaddingValues(),
    item: ListStateItem.ErrorList,
    onRetry: () -> Unit = {}
) = Box(
    modifier = boxModifier
        .padding(boxPaddingValues),
    contentAlignment = Alignment.Center
) {
    ErrorLoadingData(
        modifier = modifier,
        item = item,
        onRetry = onRetry
    )
}

@Composable
fun ErrorLoadingData(
    modifier: Modifier = Modifier,
    item: ListStateItem.ErrorList,
    showRetryButton: Boolean = true,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(23.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = item.message).addLine(item.throwable.message.toString()))
        if (showRetryButton) {
            Button(
                onClick = remember { { onRetry() } }
            ) {
                Text(text = stringResource(id = R.string.try_again_button))
            }
        }
    }
}

@Composable
fun ItemErrorPage(
    item: ListStateItem.ErrorPage,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(23.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(item.message).addLine(item.throwable.message.toString()))
        Button(
            onClick = remember { { onRetry() } }
        ) {
            Text(text = stringResource(id = R.string.try_again_button))
        }
    }
}

@Composable
fun LazyItemScope.ItemEmptyList(
    item: ListStateItem.EmptyList,
    modifier: Modifier = Modifier,
    isRefreshButtonVisible: Boolean = false,
    onRefreshClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillParentMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(23.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = item.message))
            if (isRefreshButtonVisible) {
                Button(
                    onClick = remember { { onRefreshClick() } }
                ) {
                    Text(text = stringResource(id = R.string.refresh_button))
                }
            }
        }
    }
}
