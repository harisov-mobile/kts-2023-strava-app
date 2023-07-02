package ru.internetcloud.strava.presentation.common.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LazyItemScope.ListProgressIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillParentMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
