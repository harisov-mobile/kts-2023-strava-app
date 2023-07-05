package ru.internetcloud.strava.presentation.common.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class StravaCustomColors(
    val material: Colors,
    val weak: Color
)

data class StravaCustomTypography(
    val material: Typography,
    val bodyWeak: TextStyle,
    val bodyMedium: TextStyle
)

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

private val StravaCustomDarkColorPalette = StravaCustomColors(
    material = DarkColorPalette,
    weak = Black700
)

private val StravaCustomLightColorPalette = StravaCustomColors(
    material = LightColorPalette,
    weak = Black700
)

private val stravaCustomTypography = StravaCustomTypography(
    material = Typography(),
    bodyWeak = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal
    )
)

private val LocalCustomThemeColors = compositionLocalOf { StravaCustomLightColorPalette }
private val LocalCustomTypography = compositionLocalOf { stravaCustomTypography }

@Composable
fun StravaCustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors: StravaCustomColors = if (darkTheme) {
        StravaCustomDarkColorPalette
    } else {
        StravaCustomLightColorPalette
    }

    val typography = stravaCustomTypography.copy(
        bodyWeak = stravaCustomTypography.bodyWeak.copy(
            color = colors.weak
        )
    )

    CompositionLocalProvider(
        LocalCustomThemeColors provides colors,
        LocalCustomTypography provides typography
    ) {
        MaterialTheme(
            colors = colors.material,
            typography = typography.material,
            content = content
        )
    }
}

val MaterialTheme.customColors: StravaCustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomThemeColors.current

val MaterialTheme.customTypography: StravaCustomTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomTypography.current
