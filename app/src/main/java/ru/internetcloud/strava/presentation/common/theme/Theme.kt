package ru.internetcloud.strava.presentation.common.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = tangelo,
    primaryVariant = Black900,
    secondary = Black900,
    background = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.White,
    secondaryVariant = Black500
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = tangelo,
    primaryVariant = tangelo_dark,
    secondary = Color.White,
    background = gray_light,
    onPrimary = Color.White,
    onSecondary = Black900,
    secondaryVariant = Black500
)

@Composable
fun StravaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
