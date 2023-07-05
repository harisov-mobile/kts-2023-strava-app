package ru.internetcloud.strava.presentation.common.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.Source

@Composable
fun ShowSource(source: Source) {
    if (source == Source.LocalCache) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(8.dp)
        ) {
            Card(
                shape = MaterialTheme.shapes.medium,
                backgroundColor = MaterialTheme.colors.surface,
                border = BorderStroke(width = 1.dp, color =  MaterialTheme.colors.error)
            ) {
                Text(
                    text = stringResource(id = R.string.source_description),
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}
