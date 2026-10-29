package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PureGreenPrimaryDark,
    onPrimary = PureGreenDark,
    primaryContainer = PureGreenPrimary,
    onPrimaryContainer = PureGreenContainer,
    secondary = PureWheatGold,
    secondaryContainer = PureWheatOnContainer,
    background = PureBackgroundDark,
    surface = PureSurfaceDark,
    surfaceVariant = PureSurfaceVariantDark,
    onBackground = PureTextPrimaryDark,
    onSurface = PureTextPrimaryDark,
    onSurfaceVariant = PureTextSecondaryDark,
    outline = PureTextSecondaryDark,
    error = PureError,
    errorContainer = PureErrorContainer
)

private val LightColorScheme = lightColorScheme(
    primary = PureGreenPrimary,
    onPrimary = PureSurface,
    primaryContainer = PureGreenContainer,
    onPrimaryContainer = PureGreenOnContainer,
    secondary = PureWheatGold,
    secondaryContainer = PureWheatContainer,
    onSecondaryContainer = PureWheatOnContainer,
    background = PureBackground,
    surface = PureSurface,
    surfaceVariant = PureSurfaceVariant,
    onBackground = PureTextPrimary,
    onSurface = PureTextPrimary,
    onSurfaceVariant = PureTextSecondary,
    outline = PureOutline,
    error = PureError,
    errorContainer = PureErrorContainer
)

@Composable
fun PureFoodTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent Pure Food branding by default
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
