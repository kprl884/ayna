package com.techtactoe.ayna.typography

import androidx.compose.ui.text.font.FontFamily

/**
 * Defines the expected functionality for providing platform-specific fonts.
 * Each platform (Android, iOS) will provide its own 'actual' implementation.
 */
expect object FontProvider {
    fun getSystemFontFamily(fontName: String): FontFamily
} 