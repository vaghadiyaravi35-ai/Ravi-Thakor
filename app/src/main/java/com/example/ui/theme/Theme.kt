package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkGreenPrimary,
    secondary = DarkWheatSecondary,
    tertiary = TerracottaRed,
    background = Color(0xFF1B2612),
    surface = Color(0xFF26331A),
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = FarmGreenPrimary,
    secondary = WheatGoldSecondary,
    tertiary = TerracottaRed,
    background = CreamBackground,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF1C1D1B),
    onSurface = Color(0xFF1C1D1B)
)

@Composable
fun KhetiBaadiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to maintain consistent Desi village theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
