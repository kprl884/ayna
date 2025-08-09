package com.techtactoe.ayna.common.designsystem.typography

import androidx.compose.ui.text.font.FontFamily
import com.techtactoe.ayna.typography.FontProvider

/**
 * Ayna Font Families
 * Enterprise-level font system with system font fallbacks
 */
object AppFontFamilies {

    // MARK: - Roboto (System Default)
    val roboto: FontFamily by lazy {
        FontProvider.getSystemFontFamily("Roboto")
    }

    // MARK: - Bodoni Moda (Elegant Serif)
    val bodoniModa: FontFamily by lazy {
        FontProvider.getSystemFontFamily("BodoniModa")
    }

    // MARK: - Inter (Modern Sans-Serif)
    val inter: FontFamily by lazy {
        FontProvider.getSystemFontFamily("Inter")
    }

    // MARK: - Poppins (Friendly Rounded)
    val poppins: FontFamily by lazy {
        FontProvider.getSystemFontFamily("Poppins")
    }

    // MARK: - Playfair Display (Elegant Display)
    val playfairDisplay: FontFamily by lazy {
        FontProvider.getSystemFontFamily("PlayfairDisplay")
    }

    // MARK: - Fallback Fonts
    val fallbackFont: FontFamily = FontFamily.Default
}

/**
 * Font Loading Error Handler
 * Provides fallback fonts when custom fonts fail to load
 */
object FontErrorHandler {
    fun getFallbackFont(fontFamily: FontFamily): FontFamily {
        return when (fontFamily) {
            AppFontFamilies.roboto -> FontFamily.Default
            AppFontFamilies.bodoniModa -> FontFamily.Serif
            AppFontFamilies.inter -> FontFamily.SansSerif
            AppFontFamilies.poppins -> FontFamily.SansSerif
            AppFontFamilies.playfairDisplay -> FontFamily.Serif
            else -> FontFamily.Default
        }
    }
}

/**
 * Font Metrics for consistent line spacing
 */
object FontMetrics {
    const val ROBOTO_LINE_HEIGHT_MULTIPLIER = 1.2f
    const val BODONI_LINE_HEIGHT_MULTIPLIER = 1.4f
    const val INTER_LINE_HEIGHT_MULTIPLIER = 1.3f
    const val POPPINS_LINE_HEIGHT_MULTIPLIER = 1.25f
    const val PLAYFAIR_LINE_HEIGHT_MULTIPLIER = 1.35f
}