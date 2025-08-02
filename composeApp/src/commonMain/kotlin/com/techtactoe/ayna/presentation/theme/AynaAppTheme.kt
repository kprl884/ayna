package com.techtactoe.ayna.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.techtactoe.ayna.presentation.typography.AynaTypography

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