package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Venue(
    val id: String,
    val name: String,
    val rating: Double,
    val reviewCount: Int,
    val district: String,
    val city: String,
    val images: List<String>,
    val services: List<VenueService>,
    val venueType: VenueType = VenueType.EVERYONE,
    val location: VenueLocation? = null,
    val isBookmarkSaved: Boolean = false
)

@Serializable
data class VenueService(
    val id: String,
    val name: String,
    val price: Int, // in Turkish Lira cents (e.g., 95000 = 950 TRY)
    val duration: Int, // duration in minutes
    val category: String = "General"
)

@Serializable
data class VenueLocation(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

@Serializable
enum class VenueType {
    EVERYONE,
    MALE_ONLY,
    FEMALE_ONLY
}

@Serializable
enum class SortOption {
    RECOMMENDED,
    TOP_RATED,
    NEAREST
}


@Serializable
data class PriceRange(
    val min: Int = 0,
    val max: Int = 30000 // 300 TRY in cents
)

@Serializable
data class ExploreFilters(
    val sortOption: SortOption = SortOption.RECOMMENDED,
    val priceRange: PriceRange = PriceRange(),
    val venueType: VenueType = VenueType.EVERYONE,
    val searchQuery: String = "",
    val selectedCity: String = "Istanbul"
)
