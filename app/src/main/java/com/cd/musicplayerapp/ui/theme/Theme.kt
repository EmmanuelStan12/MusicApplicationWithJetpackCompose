package com.cd.musicplayerapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorPalette = darkColors(
    primary = Blue,
    primaryVariant = DarkBlue,
    secondary = Blue,
    secondaryVariant = Blue
)

@Composable
fun MusicPlayerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = ColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}