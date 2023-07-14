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
    primaryVariant = tangelo_dark,
    onPrimary = gray_light,

    secondary = tangelo,
    secondaryVariant = Black500,
    onSecondary = gray_light,

    background = Color.Black,
    onBackground = gray_light,

    error = tangelo_light
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = tangelo,
    primaryVariant = tangelo_dark,
    onPrimary = Color.White,

    secondary = tangelo,
    secondaryVariant = Black500,
    onSecondary = Color.White,

    background = gray_light,
    onBackground = Black900,

    error = Color.Red
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
