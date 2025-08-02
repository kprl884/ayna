package com.techtactoe.ayna.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.techtactoe.ayna.presentation.typography.AynaTypography

/**
 * Ayna Design System
 * Fresha-inspired color palette and typography
 */

// MARK: - Color Palette
private val LightColorScheme = lightColorScheme(
    // Primary Colors
    primary = Color(0xFF037AFF), // Azure Radiance - Trust and action
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF0D1619),
    
    // Secondary Colors
    secondary = Color(0xFF0D1619), // Bunker - Deep charcoal for premium feel
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF5F5F5),
    onSecondaryContainer = Color(0xFF0D1619),
    
    // Background Colors
    background = Color(0xFFFFFFFF), // Pure white for cleanliness
    onBackground = Color(0xFF0D1619),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0D1619),
    
    // Error Colors
    error = Color(0xFFE53935),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = Color(0xFFC62828),
    
    // Success Colors
    surfaceVariant = Color(0xFFF8F9FA),
    onSurfaceVariant = Color(0xFF6C757D),
    
    // Additional Brand Colors
    tertiary = Color(0xFF00C853), // Success green
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE8F5E8),
    onTertiaryContainer = Color(0xFF2E7D32),
    
    // Neutral Colors
    outline = Color(0xFFE0E0E0),
    outlineVariant = Color(0xFFF5F5F5),
    scrim = Color(0x52000000),
    inverseSurface = Color(0xFF0D1619),
    inverseOnSurface = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFF90CAF9),
)

private val DarkColorScheme = darkColorScheme(
    // Primary Colors
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF0D1619),
    primaryContainer = Color(0xFF1976D2),
    onPrimaryContainer = Color(0xFFFFFFFF),
    
    // Secondary Colors
    secondary = Color(0xFFE0E0E0),
    onSecondary = Color(0xFF0D1619),
    secondaryContainer = Color(0xFF424242),
    onSecondaryContainer = Color(0xFFFFFFFF),
    
    // Background Colors
    background = Color(0xFF0D1619),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFFFFFFF),
    
    // Error Colors
    error = Color(0xFFFF6B6B),
    onError = Color(0xFF0D1619),
    errorContainer = Color(0xFF8B0000),
    onErrorContainer = Color(0xFFFFFFFF),
    
    // Success Colors
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFBDBDBD),
    
    // Additional Brand Colors
    tertiary = Color(0xFF4CAF50),
    onTertiary = Color(0xFF0D1619),
    tertiaryContainer = Color(0xFF2E7D32),
    onTertiaryContainer = Color(0xFFFFFFFF),
    
    // Neutral Colors
    outline = Color(0xFF424242),
    outlineVariant = Color(0xFF2D2D2D),
    scrim = Color(0x52000000),
    inverseSurface = Color(0xFFFFFFFF),
    inverseOnSurface = Color(0xFF0D1619),
    inversePrimary = Color(0xFF037AFF),
)


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
