package com.techtactoe.ayna.common.designsystem.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Ayna Typography System
 * Material Design 3 compliant typography with custom font families
 */
val AynaTypography = Typography(
    // Display styles for large headlines
    displayLarge = TextStyle(
        fontFamily = AppFontFamilies.playfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = (57.sp * FontMetrics.PLAYFAIR_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = AppFontFamilies.playfairDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 45.sp,
        lineHeight = (45.sp * FontMetrics.PLAYFAIR_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = AppFontFamilies.playfairDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = (36.sp * FontMetrics.PLAYFAIR_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.sp
    ),
    
    // Headline styles for section titles
    headlineLarge = TextStyle(
        fontFamily = AppFontFamilies.poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = (32.sp * FontMetrics.POPPINS_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = AppFontFamilies.poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = (28.sp * FontMetrics.POPPINS_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = AppFontFamilies.poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = (24.sp * FontMetrics.POPPINS_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.sp
    ),
    
    // Title styles for card and list headers
    titleLarge = TextStyle(
        fontFamily = AppFontFamilies.bodoniModa,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = (22.sp * FontMetrics.BODONI_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AppFontFamilies.bodoniModa,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = (16.sp * FontMetrics.BODONI_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = AppFontFamilies.bodoniModa,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = (14.sp * FontMetrics.BODONI_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.1.sp
    ),
    
    // Body styles for main content
    bodyLarge = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = (16.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = (14.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = (12.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.4.sp
    ),
    
    // Label styles for buttons and form elements
    labelLarge = TextStyle(
        fontFamily = AppFontFamilies.poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = (14.sp * FontMetrics.POPPINS_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = (12.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = (11.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.5.sp
    )
) 