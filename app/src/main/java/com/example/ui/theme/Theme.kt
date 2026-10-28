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
    primary = NovaCyan,
    onPrimary = Color(0xFF031620),
    primaryContainer = Color(0xFF083344),
    onPrimaryContainer = NovaCyan,
    secondary = NovaPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF2E1065),
    onSecondaryContainer = Color(0xFFDDD6FE),
    tertiary = NovaEmerald,
    onTertiary = Color.Black,
    background = NovaDarkBackground,
    onBackground = NovaTextPrimary,
    surface = NovaDarkSurface,
    onSurface = NovaTextPrimary,
    surfaceVariant = NovaDarkSurfaceVariant,
    onSurfaceVariant = NovaTextSecondary,
    outline = NovaDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF7C3AED),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = Color(0xFF6D28D9),
    tertiary = Color(0xFF059669),
    onTertiary = Color.White,
    background = NovaLightBackground,
    onBackground = NovaLightTextPrimary,
    surface = NovaLightSurface,
    onSurface = NovaLightTextPrimary,
    surfaceVariant = NovaLightSurfaceVariant,
    onSurfaceVariant = NovaLightTextSecondary,
    outline = NovaLightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek futuristic dark super-app theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
