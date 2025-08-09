package com.techtactoe.ayna.common.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * AynaPalette: brand and neutral color tokens (single source of hex values).
 * Do not use Material 3 role names here. These are raw tokens only.
 */
object AynaPalette {
    // Brand/base tokens (lifted from previous hardcoded values)
    val BrandPrimary = Color(0xFF037AFF)
    val BrandPrimaryContainer = Color(0xFFE3F2FD)

    // Neutral scale
    val Neutral0 = Color(0xFFFFFFFF)
    val Neutral100 = Color(0xFF000000)

    // Text and utilities
    val TextPrimary = Color(0xFF0D1619)
    val TextSecondary = Color(0xFF6C757D)
    val Border = Color(0xFFE0E0E0)
    val Inactive = Color(0xFFBDBDBD)

    // Extra semantics
    val Success = Color(0xFF00C853)
    val Warning = Color(0xFFF59E0B)
    val Error = Color(0xFFE53935)

    // Surface variants
    val SurfaceVariantLight = Color(0xFFF8F9FA)
    val SurfaceVariantDark = Color(0xFF2D2D2D)

    // Secondary family (taking from previous theme intent)
    val Secondary = Color(0xFF0D1619)
    val SecondaryContainerLight = Color(0xFFF5F5F5)
    val SecondaryContainerDark = Color(0xFF424242)

    // Dark surfaces
    val BackgroundDark = Color(0xFF0D1619)
    val SurfaceDark = Color(0xFF1A1A1A)
}
