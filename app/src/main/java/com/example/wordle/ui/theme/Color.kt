package com.example.wordle.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Brand Wordle Tile Evaluated Colors
val primary = Color(135, 183, 95)       // Green / Correct
val secondary = Color(235, 196, 84)     // Yellow / Present

// Default / Light Palette
val absent = Color(166, 174, 194)       // Gray / Absent
val unknown = Color(221, 225, 236)      // Key unpressed
val borderColor = Color(193, 197, 210)  // Empty tile border
val curBorderColor = Color(110, 115, 132) // Active typing tile border

// Dark Palette specifics
val darkBackground = Color(0xFF121213)
val darkSurface = Color(0xFF1A1A1B)
val darkOnBackground = Color(0xFFFFFFFF)
val darkOnSurface = Color(0xFFFFFFFF)
val darkOnSurfaceVariant = Color(0xFF818384)
val darkAbsent = Color(0xFF3A3A3C)
val darkUnknown = Color(0xFF818384)
val darkBorderColor = Color(0xFF3A3A3C)
val darkCurBorderColor = Color(0xFF565758)

// Light Palette specifics
val lightBackground = Color(0xFFFFFFFF)
val lightSurface = Color(0xFFFFFFFF)
val lightOnBackground = Color(0xFF1A1A1B)
val lightOnSurface = Color(0xFF1A1A1B)
val lightOnSurfaceVariant = Color(0xFF787C7E)
val lightAbsent = absent
val lightUnknown = unknown
val lightBorderColor = borderColor
val lightCurBorderColor = curBorderColor

data class WordleCustomColors(
    val primary: Color = com.example.wordle.ui.theme.primary,
    val secondary: Color = com.example.wordle.ui.theme.secondary,
    val absent: Color = lightAbsent,
    val unknown: Color = lightUnknown,
    val borderColor: Color = lightBorderColor,
    val curBorderColor: Color = lightCurBorderColor,
    val isDark: Boolean = false
)

val LocalWordleColors = staticCompositionLocalOf {
    WordleCustomColors()
}