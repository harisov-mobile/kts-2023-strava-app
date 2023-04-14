package ru.internetcloud.strava.presentation.training.list

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.model.Profile
import ru.internetcloud.strava.domain.model.TrainingListItem
import ru.internetcloud.strava.presentation.util.Calculator
import ru.internetcloud.strava.presentation.util.DateTimeConverter
import ru.internetcloud.strava.presentation.util.Formatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrainingItemView(
    modifier: Modifier = Modifier,
    profile: Profile,
    training: TrainingListItem,
    onTrainingClickListener: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onTrainingClickListener
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                AsyncImage(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    model = profile.imageUrlMedium,
                    placeholder = painterResource(id = R.drawable.no_photo),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${profile.firstName} ${profile.lastName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = DateTimeConverter.getDateTimeStringWithGMT(training.startDate),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = training.name,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            TimeDistanceSpeed(training = training)
        }
    }
}

@Composable
fun TimeDistanceSpeed(
    modifier: Modifier = Modifier,
    training: TrainingListItem
) {
    Row {
        Column {
            Text(
                text = stringResource(id = R.string.training_time_title),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = Formatter.getDetailedTime(
                    training.movingTime,
                    stringResource(id = R.string.training_hours_minutes),
                    stringResource(id = R.string.training_minutes_seconds)
                ),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
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
    title: String,
    value: String
) {
    Spacer(modifier = Modifier.width(16.dp))
    Column {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
