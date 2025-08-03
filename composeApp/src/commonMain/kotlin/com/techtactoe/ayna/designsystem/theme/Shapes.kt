package com.techtactoe.ayna.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Uygulama genelinde köşe yuvarlama için standart shape tanımları.
 * Material Design ve modern UI pratiklerine uygundur.
 */
object AynaShapes {
    /** Çok ince köşe (2.dp) — detaylar veya çok hafif yuvarlama için. */
    val extraSmall = RoundedCornerShape(2.dp)

    /** Küçük köşe (4.dp) — buton, chip gibi kompakt elemanlar için. */
    val small = RoundedCornerShape(4.dp)

    /** Orta köşe (8.dp) — kart, container gibi çoğu bileşen için varsayılan. */
    val medium = RoundedCornerShape(8.dp)

    /** Büyük köşe (16.dp) — daha belirgin yuvarlama, büyük elemanlar için. */
    val large = RoundedCornerShape(16.dp)

    /** Çok büyük köşe (32.dp) — tam yuvarlak veya yumuşak UI için. */
    val extraLarge = RoundedCornerShape(32.dp)
}

/**
 * Uygulama genelinde border kalınlıkları için merkezi sabitler.
 */
object BorderThickness {
    val extraSmall = 1.dp
    val small = 2.dp
    val medium = 3.dp
    val large = 4.dp
    val extraLarge = 5.dp
}

// MARK: - Spacing System
object Spacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val xlarge = 32.dp
    val xxlarge = 48.dp
    val xxxlarge = 64.dp
}

// MARK: - Corner Radius
object CornerRadius {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val round = 50.dp
}

// MARK: - Elevation
object Elevation {
    val xs = 1.dp
    val sm = 2.dp
    val md = 4.dp
    val lg = 8.dp
    val xl = 16.dp
}

// MARK: - Animation Durations
object AnimationDuration {
    const val fast = 150
    const val normal = 300
    const val slow = 500
}

/**
 * Ekran ve bileşen padding değerleri için merkezi sabitler.
 */
object ScreenPaddingDimensions {
    val extraSmall = 12.dp
    val small = 20.dp
    val medium = 24.dp
    val large = 32.dp
    val extraLarge = 40.dp
} 