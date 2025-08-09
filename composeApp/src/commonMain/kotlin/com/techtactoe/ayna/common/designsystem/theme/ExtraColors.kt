package com.techtactoe.ayna.common.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Immutable
data class AynaExtraColors(
    val border: Color,
    val inactive: Color,
    val success: Color,
    val warning: Color
)

val LocalAynaExtraColors = staticCompositionLocalOf {
    AynaExtraColors(
        border = AynaPalette.Border,
        inactive = AynaPalette.Inactive,
        success = AynaPalette.Success,
        warning = AynaPalette.Warning
    )
}

object AynaTheme {
    val extraColors: AynaExtraColors
        @Composable get() = LocalAynaExtraColors.current
}

/**
 * Single theme entry point: provides MaterialTheme(colorScheme) and LocalAynaExtraColors.
 * UI code must consume colors from MaterialTheme.colorScheme or AynaTheme.extraColors.
 * Only colorScheme builders should depend on AynaPalette.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean,
    typography: Typography,
    content: @Composable () -> Unit
) {
    val scheme = if (darkTheme) buildDarkColorScheme() else buildLightColorScheme()
    val extra = AynaExtraColors(
        border = AynaPalette.Border,
        inactive = AynaPalette.Inactive,
        success = AynaPalette.Success,
        warning = AynaPalette.Warning
    )
    CompositionLocalProvider(LocalAynaExtraColors provides extra) {
        MaterialTheme(
            colorScheme = scheme,
            typography = typography,
            content = content
        )
    }
}
