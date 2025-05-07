package com.bugabuga.appecommerce.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF006C51),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8FF8D3),
    onPrimaryContainer = Color(0xFF002117),
    secondary = Color(0xFF4C6358),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCEE9DA),
    onSecondaryContainer = Color(0xFF092017),
    tertiary = Color(0xFF3F6375),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFC2E8FD),
    onTertiaryContainer = Color(0xFF001F2A),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color.White,
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFBFDF9),
    onBackground = Color(0xFF191C1A),
    surface = Color(0xFFFBFDF9),
    onSurface = Color(0xFF191C1A),
    surfaceVariant = Color(0xFFDCE5DD),
    onSurfaceVariant = Color(0xFF404943),
    outline = Color(0xFF707973),
    inverseOnSurface = Color(0xFFEFF1ED),
    inverseSurface = Color(0xFF2E312F),
    inversePrimary = Color(0xFF72DBB7),
    surfaceTint = Color(0xFF006C51),
    outlineVariant = Color(0xFFC0C9C1),
    scrim = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF72DBB7),
    onPrimary = Color(0xFF00382A),
    primaryContainer = Color(0xFF00513D),
    onPrimaryContainer = Color(0xFF8FF8D3),
    secondary = Color(0xFFB2CDBE),
    onSecondary = Color(0xFF1F352B),
    secondaryContainer = Color(0xFF354B41),
    onSecondaryContainer = Color(0xFFCEE9DA),
    tertiary = Color(0xFFA6CCE0),
    onTertiary = Color(0xFF0A3544),
    tertiaryContainer = Color(0xFF264B5C),
    onTertiaryContainer = Color(0xFFC2E8FD),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF191C1A),
    onBackground = Color(0xFFE1E3DF),
    surface = Color(0xFF191C1A),
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = Color(0xFF404943),
    onSurfaceVariant = Color(0xFFC0C9C1),
    outline = Color(0xFF8A938C),
    inverseOnSurface = Color(0xFF191C1A),
    inverseSurface = Color(0xFFE1E3DF),
    inversePrimary = Color(0xFF006C51),
    surfaceTint = Color(0xFF72DBB7),
    outlineVariant = Color(0xFF404943),
    scrim = Color.Black
)

@Composable
fun AppEcommerceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
