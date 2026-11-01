package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CretoDarkColorScheme = darkColorScheme(
  primary = CretoPrimary,
  onPrimary = Color.White,
  primaryContainer = CretoSurfaceVariant,
  onPrimaryContainer = CretoPrimaryGlow,
  secondary = CretoSecondary,
  onSecondary = Color.Black,
  secondaryContainer = CretoSurfaceCard,
  onSecondaryContainer = CretoSecondaryGlow,
  tertiary = CretoAccent,
  onTertiary = Color.White,
  background = CretoBackground,
  onBackground = CretoTextPrimary,
  surface = CretoSurface,
  onSurface = CretoTextPrimary,
  surfaceVariant = CretoSurfaceVariant,
  onSurfaceVariant = CretoTextSecondary,
  outline = CretoBorder,
  outlineVariant = CretoBorderActive,
  error = CretoError,
  onError = Color.White
)

@Composable
fun CretoAiTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = CretoDarkColorScheme,
    typography = Typography,
    content = content
  )
}
