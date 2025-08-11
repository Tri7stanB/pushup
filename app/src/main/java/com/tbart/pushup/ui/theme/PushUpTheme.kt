package com.tbart.pushup.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1EB980),
    secondary = androidx.compose.ui.graphics.Color(0xFF045D56)
)

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF006D77),
    secondary = androidx.compose.ui.graphics.Color(0xFF83C5BE)
)

@Composable
fun FitnessAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
