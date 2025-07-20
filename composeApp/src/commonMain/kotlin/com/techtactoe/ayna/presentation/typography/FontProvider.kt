package com.techtactoe.ayna.presentation.typography

import androidx.compose.ui.text.font.FontFamily

/**
 * Multiplatform Font Provider Interface
 * Provides system font families for cross-platform compatibility
 */
expect object FontProvider {
    fun getSystemFontFamily(fontName: String): FontFamily
} 