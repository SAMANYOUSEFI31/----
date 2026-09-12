package com.example.bushido.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DisciplineCrimson,
    onPrimary = TextPrimary,
    primaryContainer = CardElevated,
    onPrimaryContainer = TextPrimary,
    secondary = SamuraiAmber,
    onSecondary = CanvasRoot,
    tertiary = VitalityEmerald,
    onTertiary = CanvasRoot,
    background = CanvasRoot,
    onBackground = TextPrimary,
    surface = CardElevated,
    onSurface = TextPrimary,
    surfaceVariant = CardInner,
    onSurfaceVariant = TextSecondary,
    outline = BorderStandard
)

@Composable
fun BushidoTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CanvasRoot.toArgb()
            window.navigationBarColor = CanvasRoot.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BushidoTypography,
        content = content
    )
}
