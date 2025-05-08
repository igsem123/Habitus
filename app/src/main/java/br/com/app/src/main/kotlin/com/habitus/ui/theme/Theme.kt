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
    primary = Color(0xFF00C9A7),
    onPrimary = Color.Black,
    secondary = Color(0xFF007BFF),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE1E6F2),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE1E6F2)
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007BFF),
    onPrimary = Color.White,
    secondary = Color(0xFF00C9A7),
    onSecondary = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A2D5A),
    surface = Color(0xFFF8F9FA),
    onSurface = Color(0xFF1A2D5A)
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