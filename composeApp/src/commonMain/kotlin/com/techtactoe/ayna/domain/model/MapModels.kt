package com.techtactoe.ayna.domain.model

import androidx.compose.runtime.Stable

@Stable
data class SalonMapPin(
    val salonId: String,
    val position: LatLng,
    val isSelected: Boolean = false
)

@Stable
data class SalonMapCard(
    val id: String,
    val name: String,
    val address: String,
    val location: String,
    val rating: Double,
    val reviewCount: Int,
    val distance: String,
    val imageUrl: String
)

enum class SortOptionMap {
    NEAREST,
    TOP_RATED,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW
}

enum class ServiceType {
    ALL,
    MALE_ONLY,
    FEMALE_ONLY
}

@Stable
data class MapFilterState(
    val sortBy: SortOptionMap = SortOptionMap.NEAREST,
    val serviceType: ServiceType = ServiceType.ALL
)
