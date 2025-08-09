package com.techtactoe.ayna.common.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Material 3 colorScheme builders sourced from AynaPalette tokens.
 *
 * Role glossary (when to use):
 * - primary: Main accent color (CTA/filled button, FAB, key highlights)
 * - onPrimary: Content color on primary surfaces (text/icons)
 * - primaryContainer: Secondary accent backgrounds (chips, tonal buttons)
 * - onPrimaryContainer: Content on primaryContainer
 * - secondary / onSecondary: Secondary accents and highlights
 * - secondaryContainer / onSecondaryContainer: Secondary container backgrounds
 * - background / onBackground: App/page background and its content
 * - surface / onSurface: Surfaces like cards, sheets, and their content
 * - surfaceVariant / onSurfaceVariant: Secondary surfaces (cards/tabs variants)
 * - outline / outlineVariant: Borders and subtle dividers
 * - tertiary / onTertiary: Optional tertiary accent
 * - error / onError: Error states and content
 */
fun buildLightColorScheme() = lightColorScheme(
    primary = AynaPalette.BrandPrimary,
    onPrimary = AynaPalette.Neutral0,
    primaryContainer = AynaPalette.BrandPrimaryContainer,
    onPrimaryContainer = AynaPalette.TextPrimary,

    secondary = AynaPalette.Secondary,
    onSecondary = AynaPalette.Neutral0,
    secondaryContainer = AynaPalette.SecondaryContainerLight,
    onSecondaryContainer = AynaPalette.Secondary,

    background = AynaPalette.Neutral0,
    onBackground = AynaPalette.TextPrimary,
    surface = AynaPalette.Neutral0,
    onSurface = AynaPalette.TextPrimary,
    surfaceVariant = AynaPalette.SurfaceVariantLight,
    onSurfaceVariant = AynaPalette.TextSecondary,

    outline = AynaPalette.Border,
    outlineVariant = AynaPalette.Inactive,

    tertiary = AynaPalette.Success,
    onTertiary = AynaPalette.Neutral0,

    error = AynaPalette.Error,
    onError = AynaPalette.Neutral0,

    // Keep parity with previous theme values
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = Color(0xFFC62828),
    inverseSurface = AynaPalette.Secondary,
    inverseOnSurface = AynaPalette.Neutral0,
    inversePrimary = Color(0xFF90CAF9),
    scrim = Color(0x52000000)
)

fun buildDarkColorScheme() = darkColorScheme(
    primary = AynaPalette.BrandPrimary,
    onPrimary = AynaPalette.Secondary, // dark content over primary
    primaryContainer = Color(0xFF1976D2),
    onPrimaryContainer = AynaPalette.Neutral0,

    secondary = Color(0xFFE0E0E0),
    onSecondary = AynaPalette.Secondary,
    secondaryContainer = AynaPalette.SecondaryContainerDark,
    onSecondaryContainer = AynaPalette.Neutral0,

    background = AynaPalette.BackgroundDark,
    onBackground = AynaPalette.Neutral0,
    surface = AynaPalette.SurfaceDark,
    onSurface = AynaPalette.Neutral0,
    surfaceVariant = AynaPalette.SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFBDBDBD),

    outline = AynaPalette.SecondaryContainerDark,
    outlineVariant = AynaPalette.SurfaceVariantDark,

    tertiary = Color(0xFF4CAF50),
    onTertiary = AynaPalette.Secondary,

    error = AynaPalette.Error,
    onError = AynaPalette.Secondary,

    errorContainer = Color(0xFF8B0000),
    onErrorContainer = AynaPalette.Neutral0,
    inverseSurface = AynaPalette.Neutral0,
    inverseOnSurface = AynaPalette.Secondary,
    inversePrimary = AynaPalette.BrandPrimary,
    scrim = Color(0x52000000)
)
