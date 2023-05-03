package ru.internetcloud.strava.presentation.main.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LazyItemScope.LoadingData(
    modifier: Modifier = Modifier,
    fillParentMaxSize: Boolean = true
) {
    Column(
        modifier = modifier
            .fillParentMaxWidth()
            .applyIf(fillParentMaxSize) {
                fillParentMaxSize()
            }
            .padding(23.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

inline fun <T> T.applyIf(predicate: Boolean, block: T.() -> T): T {
    return if (predicate) block() else this
}
