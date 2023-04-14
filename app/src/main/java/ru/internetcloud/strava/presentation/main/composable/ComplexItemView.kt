package ru.internetcloud.strava.presentation.main.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.main.model.ComplexItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ComplexItemView(item: ComplexItem, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(16.dp)
            .animateItemPlacement(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            modifier = Modifier
                .padding(vertical = 6.dp),
            painter = painterResource(id = R.drawable.no_photo),
            contentDescription = null
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.author,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "id = ${item.id}"
            )
        }
    }
}
