package ru.internetcloud.strava.presentation.common.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.Source

@Composable
fun ShowSource(source: Source) {
    if (source == Source.LocalCache) {
        Text(
            text = stringResource(id = R.string.source_description),
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Red,
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 4.dp,
                        bottomEnd = 4.dp,
                        bottomStart = 4.dp
                    )
                )
                .padding(8.dp),
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
