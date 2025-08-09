package com.techtactoe.ayna.common.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography

@Composable
fun AynaAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AynaTypography,
        content = content
    )
}