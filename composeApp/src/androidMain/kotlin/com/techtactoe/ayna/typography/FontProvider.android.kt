package com.techtactoe.ayna.typography

import androidx.compose.ui.text.font.FontFamily

/**
 * Android Font Provider Implementation
 * Uses system fonts for cross-platform compatibility
 */
actual object FontProvider {
    actual fun getSystemFontFamily(fontName: String): FontFamily {
        return when (fontName) {
            "Roboto" -> FontFamily.Default
            "BodoniModa" -> FontFamily.Serif
            "Inter" -> FontFamily.SansSerif
            "Poppins" -> FontFamily.SansSerif
            "PlayfairDisplay" -> FontFamily.Serif
            else -> FontFamily.Default
        }
    }
} 