package com.example.wordle.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.wordle.domain.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = primary,
    secondary = secondary,
    background = darkBackground,
    surface = darkSurface,
    onBackground = darkOnBackground,
    onSurface = darkOnSurface,
    onSurfaceVariant = darkOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = primary,
    secondary = secondary,
    background = lightBackground,
    surface = lightSurface,
    onBackground = lightOnBackground,
    onSurface = lightOnSurface,
    onSurfaceVariant = lightOnSurfaceVariant
)

@Composable
fun WordleTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    val customColors = if (isDark) {
        WordleCustomColors(
            primary = primary,
            secondary = secondary,
            absent = darkAbsent,
            unknown = darkUnknown,
            borderColor = darkBorderColor,
            curBorderColor = darkCurBorderColor,
            isDark = true
        )
    } else {
        WordleCustomColors(
            primary = primary,
            secondary = secondary,
            absent = lightAbsent,
            unknown = lightUnknown,
            borderColor = lightBorderColor,
            curBorderColor = lightCurBorderColor,
            isDark = false
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(LocalWordleColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}