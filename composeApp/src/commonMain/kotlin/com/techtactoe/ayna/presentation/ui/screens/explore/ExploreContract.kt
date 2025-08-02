package com.techtactoe.ayna.presentation.ui.screens.explore

import com.techtactoe.ayna.domain.model.ExploreFilters
import com.techtactoe.ayna.domain.model.Venue
import com.techtactoe.ayna.presentation.ui.screens.explore.BottomSheetType

/**
 * Contract defining the UI state and events for the Explore screen
 * Following the standardized MVVM pattern
 */
interface ExploreContract {
    
    /**
     * Single source of truth for all UI state in the Explore screen
     */
    data class UiState(
        // Data lists
        val venues: List<Venue> = emptyList(),
        val filters: ExploreFilters = ExploreFilters(),
        val tempFilters: ExploreFilters = ExploreFilters(),
        
        // UI interaction state
        val currentBottomSheet: BottomSheetType = BottomSheetType.None,
        val searchQuery: String = "",
        val selectedCity: String = "Istanbul",
        
        // Loading states
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val hasMorePages: Boolean = true,
        val isLocationPermissionGranted: Boolean = false,
        
        // Navigation flags
        val navigateToVenueDetail: String? = null,
        val navigateToMap: Boolean = false,
        val navigateToAdvancedSearch: Boolean = false,
        
        // Error and success states
        val errorMessage: String? = null,
        val isSuccess: Boolean = false,
        val snackbarMessage: String? = null
    ) {
        val isAnyBottomSheetVisible: Boolean
            get() = currentBottomSheet != BottomSheetType.None
    }
    
    /**
     * All possible user interactions with the Explore screen
     */
    sealed interface UiEvent {
        // Filter events
        data class OnShowBottomSheet(val type: BottomSheetType) : UiEvent
        data object OnHideBottomSheet : UiEvent
        data class OnUpdateTempFilters(val filters: ExploreFilters) : UiEvent
        data object OnApplyTempFilters : UiEvent
        data object OnClearFilters : UiEvent
        
        // Search events
        data class OnSearchQueryChanged(val query: String) : UiEvent
        data class OnSelectedCityChanged(val city: String) : UiEvent
        
        // Venue interaction events
        data class OnVenueClicked(val venueId: String) : UiEvent
        data class OnVenueBookmarked(val venueId: String) : UiEvent
        
        // Navigation events
        data object OnNavigateToMap : UiEvent
        data object OnNavigateToAdvancedSearch : UiEvent
        data class OnNavigationHandled(val resetNavigation: NavigationReset) : UiEvent
        
        // List events
        data object OnRefreshVenues : UiEvent
        data object OnLoadMoreVenues : UiEvent
        
        // Permission events
        data object OnRequestLocationPermission : UiEvent
        
        // Error handling
        data object OnClearError : UiEvent
        data object OnSnackbarDismissed : UiEvent
    }
    
    /**
     * Navigation reset options
     */
    enum class NavigationReset {
        VENUE_DETAIL,
        MAP,
        ADVANCED_SEARCH
    }
}
