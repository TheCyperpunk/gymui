package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ByceDarkColorScheme =
  darkColorScheme(
    primary = ByceGreen,
    onPrimary = DarkNavy,
    primaryContainer = GlassSurfaceMedium,
    onPrimaryContainer = TextWhite,
    secondary = GlassSurfaceLight,
    onSecondary = TextWhite,
    background = DarkNavy,
    onBackground = TextWhite,
    surface = DarkNavyDepth,
    onSurface = TextWhite,
    surfaceVariant = CharcoalSurface,
    onSurfaceVariant = TextMuted,
    outline = GlassBorderLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = ByceDarkColorScheme, typography = Typography, content = content)
}

