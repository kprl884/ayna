package com.techtactoe.ayna.presentation.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Ayna Semantic Text Styles
 * Enterprise-level text style system for beauty app UI
 */
object AppTextStyles {
    
    // MARK: - App Title/Branding (Playfair Display Bold)
    val appTitle = TextStyle(
        fontFamily = AppFontFamilies.playfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = (32.sp * FontMetrics.PLAYFAIR_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = (-0.25).sp
    )
    
    val appSubtitle = TextStyle(
        fontFamily = AppFontFamilies.playfairDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = (18.sp * FontMetrics.PLAYFAIR_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.sp
    )
    
    // MARK: - Category Headers (Poppins SemiBold)
    val categoryTitle = TextStyle(
        fontFamily = AppFontFamilies.poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = (24.sp * FontMetrics.POPPINS_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.sp
    )
    
    val sectionTitle = TextStyle(
        fontFamily = AppFontFamilies.poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = (18.sp * FontMetrics.POPPINS_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.sp
    )
    
    // MARK: - Business Names (Bodoni Moda Medium)
    val businessName = TextStyle(
        fontFamily = AppFontFamilies.bodoniModa,
        fontWeight = FontWeight.Medium,
        fontSize = 36.sp,
        lineHeight = (36.sp * FontMetrics.BODONI_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.15.sp
    )
    
    val businessNameLarge = TextStyle(
        fontFamily = AppFontFamilies.bodoniModa,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = (20.sp * FontMetrics.BODONI_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.15.sp
    )
    
    // MARK: - Descriptions/Body Text (Inter Regular)
    val bodyText = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = (14.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.25.sp
    )
    
    val bodyTextLarge = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = (16.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.5.sp
    )
    
    val bodyTextSmall = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = (12.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.4.sp
    )
    
    // MARK: - Ratings/Numbers (Roboto Medium)
    val rating = TextStyle(
        fontFamily = AppFontFamilies.roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = (14.sp * FontMetrics.ROBOTO_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.1.sp
    )
    
    val price = TextStyle(
        fontFamily = AppFontFamilies.roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = (16.sp * FontMetrics.ROBOTO_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.1.sp
    )
    
    val priceLarge = TextStyle(
        fontFamily = AppFontFamilies.roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = (20.sp * FontMetrics.ROBOTO_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.1.sp
    )
    
    // MARK: - Buttons (Poppins Medium)
    val buttonText = TextStyle(
        fontFamily = AppFontFamilies.poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = (14.sp * FontMetrics.POPPINS_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.1.sp
    )
    
    val buttonTextLarge = TextStyle(
        fontFamily = AppFontFamilies.poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = (16.sp * FontMetrics.POPPINS_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.1.sp
    )
    
    // MARK: - Navigation (Inter Medium)
    val navigationText = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = (12.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.5.sp
    )
    
    val navigationTextSelected = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = (12.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.5.sp
    )
    
    // MARK: - Form Elements
    val inputLabel = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = (12.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.5.sp
    )
    
    val inputText = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = (14.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.25.sp
    )
    
    val inputPlaceholder = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = (14.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.25.sp
    )
    
    // MARK: - Special Use Cases
    val splashTitle = TextStyle(
        fontFamily = AppFontFamilies.playfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = (36.sp * FontMetrics.PLAYFAIR_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = (-0.25).sp
    )
    
    val splashSubtitle = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = (16.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.5.sp
    )
    
    val errorText = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = (12.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.4.sp
    )
    
    val successText = TextStyle(
        fontFamily = AppFontFamilies.inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = (12.sp * FontMetrics.INTER_LINE_HEIGHT_MULTIPLIER),
        letterSpacing = 0.4.sp
    )
} 