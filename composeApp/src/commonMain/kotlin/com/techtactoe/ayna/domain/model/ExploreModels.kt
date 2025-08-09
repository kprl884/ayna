package com.techtactoe.ayna.domain.model

import androidx.compose.runtime.Stable

/**
 * Enhanced domain models for Explore feature
 * Following SOLID principles and performance optimization
 */

@Stable
data class VenueServiceUiModel(
    val id: String,
    val name: String,
    val duration: Int, // in minutes
    val price: Int, // in cents
    val isAvailable: Boolean = true
)

@Stable
data class ExploreFiltersUiModel(
    val searchQuery: String = "",
    val selectedCity: String = "",
    val sortOption: SortOption = SortOption.RECOMMENDED,
    val priceRange: PriceRangeUiModel = PriceRangeUiModel(),
    val venueType: VenueType = VenueType.EVERYONE
)

@Stable
data class PriceRangeUiModel(
    val min: Int = 0,
    val max: Int = 30000
)

sealed class ExploreError : Exception() {
    data object NetworkError : ExploreError()
    data object LocationPermissionDenied : ExploreError()
    data object NoDataError : ExploreError()
    data class UnknownError(override val message: String) : ExploreError()
}

@Stable
data class ExploreContentState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val venues: List<Venue> = emptyList(),
    val hasMorePages: Boolean = false,
    val error: ExploreError? = null
)

@Stable
data class ExploreFilterState(
    val filters: ExploreFiltersUiModel = ExploreFiltersUiModel(),
    val tempFilters: ExploreFiltersUiModel = ExploreFiltersUiModel(),
    val isBottomSheetVisible: Boolean = false,
    val bottomSheetType: BottomSheetType = BottomSheetType.None
)

@Stable
data class ExploreNavigationState(
    val navigateToVenueDetail: String? = null,
    val navigateToMap: Boolean = false,
    val navigateToAdvancedSearch: Boolean = false,
    val snackbarMessage: String? = null
)

@Stable
data class ExploreInteractionState(
    val isLocationPermissionGranted: Boolean = false,
    val selectedVenueId: String? = null
)


