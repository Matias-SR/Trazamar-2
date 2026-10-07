package cl.aldemar.trazamar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BayPrimary,
    onPrimary = Tide,
    primaryContainer = BaySoft,
    onPrimaryContainer = BayDeep,
    secondary = KelpSecondary,
    onSecondary = Tide,
    secondaryContainer = KelpSoft,
    onSecondaryContainer = Ink,
    tertiary = Amber,
    onTertiary = Ink,
    tertiaryContainer = AmberSoft,
    background = Tide,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = BaySoft,
    onSurfaceVariant = InkMuted,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorSoft,
    onErrorContainer = ErrorRed,
    outline = LineSubtle,
)

private val DarkColors = darkColorScheme(
    primary = BaySoft,
    onPrimary = BayDeep,
    primaryContainer = BayPrimary,
    onPrimaryContainer = Tide,
    secondary = KelpSecondary,
    onSecondary = Ink,
    background = BayDeep,
    onBackground = Tide,
    surface = Color(0xFF0F2E3A),
    onSurface = Tide,
    error = ErrorRed,
    onError = Tide,
)

@Composable
fun TrazaMarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = TrazaMarTypography,
        content = content
    )
}
