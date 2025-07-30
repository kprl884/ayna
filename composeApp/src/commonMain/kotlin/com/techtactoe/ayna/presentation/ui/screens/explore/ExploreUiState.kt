package com.techtactoe.ayna.presentation.ui.screens.explore

import com.techtactoe.ayna.domain.model.ExploreFilters
import com.techtactoe.ayna.domain.model.Venue

sealed interface ExploreUiState {
    data object Loading : ExploreUiState
    data class Success(
        val isLoading: Boolean = false,
        val venues: List<Venue>,
        val isRefreshing: Boolean = false,
        val hasMorePages: Boolean = true,
        val filters: ExploreFilters = ExploreFilters(),
        val isLocationPermissionGranted: Boolean = false
    ) : ExploreUiState
    data class Error(
        val message: String,
        val filters: ExploreFilters = ExploreFilters()
    ) : ExploreUiState
    data class Empty(
        val message: String = "We didn't find a match",
        val subMessage: String = "Try a new search",
        val filters: ExploreFilters = ExploreFilters()
    ) : ExploreUiState
}

sealed interface ExploreIntent {
    data object LoadVenues : ExploreIntent
    data object RefreshVenues : ExploreIntent
    data object LoadMoreVenues : ExploreIntent
    data class UpdateFilters(val filters: ExploreFilters) : ExploreIntent
    data class UpdateSearchQuery(val query: String) : ExploreIntent
    data class UpdateSelectedCity(val city: String) : ExploreIntent
    data object ClearFilters : ExploreIntent
    data class BookmarkVenue(val venueId: String) : ExploreIntent
    data object RequestLocationPermission : ExploreIntent
}

sealed interface ExploreEvent {
    data class NavigateToVenueDetail(val venueId: String) : ExploreEvent
    data object NavigateToMap : ExploreEvent
    data object NavigateToAdvancedSearch : ExploreEvent
    data class ShowSnackbar(val message: String) : ExploreEvent
}

sealed interface BottomSheetType {
    data object None : BottomSheetType
    data object Sort : BottomSheetType
    data object Price : BottomSheetType
    data object VenueType : BottomSheetType
    data object Filters : BottomSheetType
}

data class ExploreScreenState(
    val uiState: ExploreUiState = ExploreUiState.Loading,
    val currentBottomSheet: BottomSheetType = BottomSheetType.None,
    val tempFilters: ExploreFilters = ExploreFilters() // Temporary filters for bottom sheets
)
