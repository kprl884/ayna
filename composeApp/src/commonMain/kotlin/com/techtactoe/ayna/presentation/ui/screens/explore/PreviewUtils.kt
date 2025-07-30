package com.techtactoe.ayna.presentation.ui.screens.explore

import com.techtactoe.ayna.domain.model.*

/**
 * Sample data provider for previews and testing
 */
fun sampleVenues(): List<Venue> = listOf(
    Venue(
        id = "1",
        name = "Emre's Barbershop Dolapdere",
        rating = 5.0,
        reviewCount = 69,
        district = "Dolapdere",
        city = "Istanbul",
        images = listOf(
            "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=800",
            "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=800",
            "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800"
        ),
        services = listOf(
            VenueService("1", "Haircut / Saç Kesimi", 70000, 60),
            VenueService("2", "Haircut & Shave / Saç Kesimi & Sakal Tıraşı", 117500, 90),
            VenueService("3", "Full Service / Komple Bakım", 188000, 120)
        ),
        venueType = VenueType.MALE_ONLY,
        location = VenueLocation(41.0082, 28.9784, "Dolapdere, Istanbul")
    ),
    Venue(
        id = "2",
        name = "Ayna Ayna",
        rating = 5.0,
        reviewCount = 158,
        district = "Fenerbahçe",
        city = "Istanbul",
        images = listOf(
            "https://images.unsplash.com/photo-1580618672591-eb180b1a973f?w=800",
            "https://images.unsplash.com/photo-1522336284037-91f7da073525?w=800"
        ),
        services = listOf(
            VenueService("4", "Spa Pedikür (Spa Pedicure)", 95000, 55),
            VenueService("5", "SMART Manikür + Kalıcı Oje (Smart Mani + Gel Polish)", 120000, 100),
            VenueService("6", "Nail Art", 0, 20)
        ),
        venueType = VenueType.FEMALE_ONLY,
        location = VenueLocation(40.9789, 29.0372, "Fenerbahçe, Rüştiye Sokak 9, Istanbul")
    ),
    Venue(
        id = "3",
        name = "Serenity Spa & Wellness",
        rating = 4.8,
        reviewCount = 234,
        district = "Bebek",
        city = "Istanbul",
        images = listOf(
            "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=800",
            "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800"
        ),
        services = listOf(
            VenueService("7", "60 Min Thai with Oil Massage", 160000, 75),
            VenueService("8", "30 Min Foot Massage", 80000, 30),
            VenueService("9", "60 Min Oil Massage", 160000, 75)
        ),
        venueType = VenueType.EVERYONE,
        location = VenueLocation(41.0766, 29.0434, "Bebek, Istanbul")
    ),
    Venue(
        id = "4",
        name = "Elite Hair Studio",
        rating = 4.7,
        reviewCount = 89,
        district = "Nişantaşı",
        city = "Istanbul",
        images = listOf(
            "https://images.unsplash.com/photo-1562322140-8baeececf3df?w=800"
        ),
        services = listOf(
            VenueService("10", "Women's Cut & Style", 200000, 90),
            VenueService("11", "Color Treatment", 350000, 180),
            VenueService("12", "Blowout", 100000, 45)
        ),
        venueType = VenueType.EVERYONE,
        location = VenueLocation(41.0460, 28.9940, "Nişantaşı, Istanbul")
    ),
    Venue(
        id = "5",
        name = "Gentleman's Lounge",
        rating = 4.6,
        reviewCount = 156,
        district = "Galata",
        city = "Istanbul",
        images = listOf(
            "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800"
        ),
        services = listOf(
            VenueService("13", "Classic Shave", 80000, 30),
            VenueService("14", "Beard Trim", 60000, 20),
            VenueService("15", "Premium Package", 150000, 75)
        ),
        venueType = VenueType.MALE_ONLY,
        location = VenueLocation(41.0256, 28.9744, "Galata, Istanbul")
    )
)

/**
 * Preview parameter provider for ExploreScreen previews
 */
class ExploreScreenPreviewProvider : androidx.compose.ui.tooling.preview.PreviewParameterProvider<ExploreUiState> {
    override val values: Sequence<ExploreUiState> = sequenceOf(
        // Default state with venues
        ExploreUiState.Success(
            venues = sampleVenues(),
            isRefreshing = false,
            hasMorePages = true,
            filters = ExploreFilters(),
            isLocationPermissionGranted = false
        ),
        // Loading state
        ExploreUiState.Loading,
        // Empty state
        ExploreUiState.Empty(),
        // Error state
        ExploreUiState.Error("Network connection failed")
    )
}
