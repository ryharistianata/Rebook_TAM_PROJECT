package com.kelompok.rebook.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary              = Primary40,
    onPrimary            = White,
    primaryContainer     = PrimaryContainer40,
    onPrimaryContainer   = Primary20,
    secondary            = Secondary40,
    onSecondary          = White,
    tertiary             = Tertiary40,
    background           = PaperLight,
    surface              = PaperLight,
    onBackground         = InkDark,
    onSurface            = InkDark,
    surfaceVariant       = NeutralVariant90,
    onSurfaceVariant     = NeutralVariant30,
)

private val DarkColorScheme = darkColorScheme(
    primary              = Primary80,
    onPrimary            = Primary20,
    primaryContainer     = PrimaryContainer20,
    onPrimaryContainer   = Primary90,
    secondary            = Secondary80,
    tertiary             = Tertiary80,
    background           = PaperDark,
    surface              = PaperDark,
    onBackground         = Neutral90,
    onSurface            = Neutral90,
)

@Composable
fun ReBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = ReBookTypography,
        content     = content
    )
}