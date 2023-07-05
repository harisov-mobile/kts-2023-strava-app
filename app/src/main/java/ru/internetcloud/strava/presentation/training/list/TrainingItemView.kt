package ru.internetcloud.strava.presentation.training.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.koin.androidx.compose.inject
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.util.DateConverter
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.training.model.TrainingListItem
import ru.internetcloud.strava.presentation.common.theme.customTypography
import ru.internetcloud.strava.presentation.util.Calculator
import ru.internetcloud.strava.presentation.util.Formatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrainingItemView(
    modifier: Modifier = Modifier,
    profile: Profile,
    training: TrainingListItem,
    onTrainingClickListener: () -> Unit
) {
    val dateConverter: DateConverter by inject()

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onTrainingClickListener
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.big_margin))
        ) {
            Row {
                AsyncImage(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.training_item_icon_size))
                        .clip(CircleShape),
                    model = profile.imageUrlMedium,
                    placeholder = painterResource(id = R.drawable.no_photo),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.big_margin)))
                Column {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_margin)))
                    Text(
                        text = "${profile.firstName} ${profile.lastName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = dateConverter.getDateTimeStringWithGMT(training.startDate),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_margin)))
            Text(
                text = training.name,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_margin)))
            TimeDistanceSpeed(training = training)
        }
    }
}

@Composable
fun TimeDistanceSpeed(
    modifier: Modifier = Modifier,
    training: TrainingListItem
) {
    Row(
        modifier = modifier
    ) {
        ShowPart(
            title = stringResource(id = R.string.training_time_title),
            value = Formatter.getDetailedTime(
                training.movingTime,
                stringResource(id = R.string.training_hours_minutes),
                stringResource(id = R.string.training_minutes_seconds)
            ),
            enableStartPadding = false
        )
        if (training.distance > 0) {
            ShowPart(
                title = stringResource(id = R.string.training_distance_title),
                value = Formatter.getFormattedValue(
                    value = Calculator.calculateDistance(training.distance),
                    format = stringResource(id = R.string.training_distance_format)
                )
            )
        }
        if (training.distance > 0 && training.movingTime > 0) {
            ShowPart(
                title = stringResource(id = R.string.training_speed_title),
                value = Formatter.getFormattedValue(
                    value = Calculator.calculateSpeed(distance = training.distance, movingTime = training.movingTime),
                    format = stringResource(id = R.string.training_speed_format)
                )
            )
        }
    }
}

@Composable
fun ShowPart(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    enableStartPadding: Boolean = true
) {
    val startPadding = if (enableStartPadding) dimensionResource(R.dimen.big_margin) else 0.dp
    Box(modifier = modifier.padding(start = startPadding)) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.caption
            )
            Text(
                text = value,
                style = MaterialTheme.customTypography.bodyMedium
            )
        }
    }
}
