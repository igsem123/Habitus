package br.com.app.src.main.kotlin.com.habitus.ui.theme

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

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2B5983),
    onPrimary = Color(0xF5F5F5F5),
    primaryContainer = Color(0xFF294B80),
    onPrimaryContainer = Color(0xFFD0E4FF),

    secondary = Color(0xFF65E8BE),
    onSecondary = Color(0xFF00382E),
    secondaryContainer = Color(0xFF005143),
    onSecondaryContainer = Color(0xFF9CF1D9),

    tertiary = Color(0xFFABC7FF),
    onTertiary = Color(0xFF002F68),
    tertiaryContainer = Color(0xFF00458F),
    onTertiaryContainer = Color(0xFFD8E2FF),

    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE3E2E6),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C7CF),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    inverseSurface = Color(0xFFE3E2E6),

    outline = Color(0xFF8D9199)
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF003366),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0E4FF),
    onPrimaryContainer = Color(0xFF001D36),

    secondary = Color(0xFF00C9A7),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF9CF1D9),
    onSecondaryContainer = Color(0xFF00201A),

    tertiary = Color(0xFF007BFF),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD8E2FF),
    onTertiaryContainer = Color(0xFF001A41),

    background = Color(0xFFFDFBFF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFDFBFF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDFE2EB),
    onSurfaceVariant = Color(0xFF43474E),

    inverseSurface = Color(0xFF2F3033),

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = Color(0xFF73777F)
)


@Composable
fun HabitusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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