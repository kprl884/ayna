package com.techtactoe.ayna.presentation.ui.screens.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.domain.model.LatLng
import com.techtactoe.ayna.domain.model.SalonMapPin

/**
 * MapView component - Platform-specific implementation needed
 * 
 * For production use:
 * - Android: Use Google Maps Compose (com.google.maps.android:maps-compose)
 * - iOS: Use MapKit through expect/actual pattern
 */
@Composable
expect fun MapView(
    pins: List<SalonMapPin>,
    center: LatLng,
    onPinClick: (String) -> Unit,
    modifier: Modifier = Modifier
)
