package com.techtactoe.ayna.presentation.ui.screens.explore

import androidx.compose.runtime.Stable
import com.techtactoe.ayna.domain.model.BottomSheetType
import com.techtactoe.ayna.domain.model.ExploreContentState
import com.techtactoe.ayna.domain.model.ExploreFilterState
import com.techtactoe.ayna.domain.model.ExploreFiltersUiModel
import com.techtactoe.ayna.domain.model.ExploreInteractionState
import com.techtactoe.ayna.domain.model.ExploreNavigationState
import com.techtactoe.ayna.domain.model.SortOption
import com.techtactoe.ayna.domain.model.VenueType

/**
 * Enhanced Contract for Explore feature following SOLID principles
 * Improved separation of concerns and type safety
 */
interface ExploreContract{
    
    @Stable
    data class UiState(
        val contentState: ExploreContentState = ExploreContentState(),
        val filterState: ExploreFilterState = ExploreFilterState(),
        val navigationState: ExploreNavigationState = ExploreNavigationState(),
        val interactionState: ExploreInteractionState = ExploreInteractionState()
    )
    
    sealed interface UiEvent {
        // Content Events
        data object OnRefreshVenues : UiEvent
        data object OnLoadMoreVenues : UiEvent
        data object OnRetryLoadVenues : UiEvent
        
        // Filter Events
        data class OnShowBottomSheet(val type: BottomSheetType) : UiEvent
        data object OnHideBottomSheet : UiEvent
        data class OnUpdateTempFilters(val filters: ExploreFiltersUiModel) : UiEvent
        data object OnApplyTempFilters : UiEvent
        data object OnClearAllFilters : UiEvent
        
        // Quick Filter Events
        data class OnSortOptionSelected(val option: SortOption) : UiEvent
        data class OnVenueTypeSelected(val type: VenueType) : UiEvent
        data class OnPriceRangeSelected(val minPrice: Int, val maxPrice: Int) : UiEvent
        
        // Search Events
        data class OnSearchQueryChanged(val query: String) : UiEvent
        data class OnCitySelected(val city: String) : UiEvent
        
        // Venue Interaction Events
        data class OnVenueClicked(val venueId: String) : UiEvent
        data class OnVenueBookmarked(val venueId: String) : UiEvent
        data class OnServiceClicked(val venueId: String, val serviceId: String) : UiEvent
        
        // Navigation Events
        data object OnNavigateToMap : UiEvent
        data object OnNavigateToAdvancedSearch : UiEvent
        data class OnNavigationHandled(val resetType: NavigationReset) : UiEvent
        
        // Permission Events
        data object OnRequestLocationPermission : UiEvent
        data object OnLocationPermissionGranted : UiEvent
        data object OnLocationPermissionDenied : UiEvent
        
        // Error & Message Events
        data object OnErrorDismissed : UiEvent
        data object OnSnackbarDismissed : UiEvent
    }
    
    sealed interface UiEffect {
        data class NavigateToVenueDetail(val venueId: String) : UiEffect
        data object NavigateToMap : UiEffect
        data object NavigateToAdvancedSearch : UiEffect
        data class ShowSnackbar(val message: String) : UiEffect
        data object RequestLocationPermission : UiEffect
        data class ShareVenue(val venueId: String, val venueName: String) : UiEffect
    }
    
    enum class NavigationReset {
        VENUE_DETAIL,
        MAP,
        ADVANCED_SEARCH
    }
}
