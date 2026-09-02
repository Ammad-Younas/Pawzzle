package com.madi.pawzzle.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = CatOrange,
    onPrimary = Color.White,
    primaryContainer = CatOrangeContainer,
    onPrimaryContainer = OnCatOrangeContainer,
    secondary = GrassGreen,
    onSecondary = Color.White,
    secondaryContainer = GrassGreenContainer,
    onSecondaryContainer = OnGrassGreenContainer,
    tertiary = SkyBlue,
    onTertiary = Color.White,
    tertiaryContainer = SkyBlueContainer,
    onTertiaryContainer = OnSkyBlueContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
)

@Composable
fun PawzzleTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}