package com.smartassistant.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = CyanAccent,
    tertiary = PurpleAI,
    background = NavyDark,
    surface = DeepBlue,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = CyanAccent,
    tertiary = PurpleAI,
    background = AppBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = NavyDark,
    onSurface = NavyDark
)

@Composable
fun SmartAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colors, content = content)
}
