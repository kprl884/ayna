package com.techtactoe.ayna.presentation.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.data.FakeExploreRepository
import com.techtactoe.ayna.domain.model.BottomSheetType
import com.techtactoe.ayna.domain.model.ExploreError
import com.techtactoe.ayna.domain.model.ExploreFilters
import com.techtactoe.ayna.domain.model.ExploreFiltersUiModel
import com.techtactoe.ayna.domain.model.PriceRangeUiModel
import com.techtactoe.ayna.domain.model.SortOption
import com.techtactoe.ayna.domain.model.VenueType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Enhanced ExploreViewModel following the new ExploreContract
 *
 * Key Improvements:
 * - SOLID principles compliance with clear separation of concerns
 * - Enhanced state management with separate state categories
 * - Better error handling with typed errors
 * - Performance optimization with efficient state updates
 * - Use case pattern for business logic (to be integrated)
 */
class ExploreViewModel(
    private val repository: FakeExploreRepository = FakeExploreRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreContract.UiState())
    val uiState: StateFlow<ExploreContract.UiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private var isLoadingMore = false

    init {
        loadVenues(isRefresh = false)
    }

    fun onEvent(event: ExploreContract.UiEvent) {
        when (event) {
            // Content Events
            is ExploreContract.UiEvent.OnRefreshVenues -> {
                loadVenues(isRefresh = true)
            }

            is ExploreContract.UiEvent.OnLoadMoreVenues -> {
                if (!isLoadingMore && _uiState.value.contentState.hasMorePages) {
                    loadMoreVenues()
                }
            }

            is ExploreContract.UiEvent.OnRetryLoadVenues -> {
                clearError()
                loadVenues(isRefresh = false)
            }

            // Filter Events
            is ExploreContract.UiEvent.OnShowBottomSheet -> {
                showBottomSheet(event.type)
            }

            is ExploreContract.UiEvent.OnHideBottomSheet -> {
                hideBottomSheet()
            }

            is ExploreContract.UiEvent.OnUpdateTempFilters -> {
                updateTempFilters(event.filters)
            }

            is ExploreContract.UiEvent.OnApplyTempFilters -> {
                applyTempFilters()
            }

            is ExploreContract.UiEvent.OnClearAllFilters -> {
                clearAllFilters()
            }

            // Quick Filter Events
            is ExploreContract.UiEvent.OnSortOptionSelected -> {
                updateSortOption(event.option)
            }

            is ExploreContract.UiEvent.OnVenueTypeSelected -> {
                updateVenueType(event.type)
            }

            is ExploreContract.UiEvent.OnPriceRangeSelected -> {
                updatePriceRange(event.minPrice, event.maxPrice)
            }

            // Search Events
            is ExploreContract.UiEvent.OnSearchQueryChanged -> {
                updateSearchQuery(event.query)
            }

            is ExploreContract.UiEvent.OnCitySelected -> {
                updateSelectedCity(event.city)
            }

            // Venue Interaction Events
            is ExploreContract.UiEvent.OnVenueClicked -> {
                navigateToVenueDetail(event.venueId)
            }

            is ExploreContract.UiEvent.OnVenueBookmarked -> {
                toggleVenueBookmark(event.venueId)
            }

            is ExploreContract.UiEvent.OnServiceClicked -> {
                // TODO: Handle service click logic
                showSnackbar("Service clicked: ${event.serviceId}")
            }

            // Navigation Events
            is ExploreContract.UiEvent.OnNavigateToMap -> {
                navigateToMap()
            }

            is ExploreContract.UiEvent.OnNavigateToAdvancedSearch -> {
                navigateToAdvancedSearch()
            }

            is ExploreContract.UiEvent.OnNavigationHandled -> {
                clearNavigationState(event.resetType)
            }

            // Permission Events
            is ExploreContract.UiEvent.OnRequestLocationPermission -> {
                // TODO: Handle location permission request
            }

            is ExploreContract.UiEvent.OnLocationPermissionGranted -> {
                updateLocationPermissionGranted(true)
                loadVenues(isRefresh = true) // Reload with location data
            }

            is ExploreContract.UiEvent.OnLocationPermissionDenied -> {
                updateLocationPermissionGranted(false)
                showError(ExploreError.LocationPermissionDenied)
            }

            // Error & Message Events
            is ExploreContract.UiEvent.OnErrorDismissed -> {
                clearError()
            }

            is ExploreContract.UiEvent.OnSnackbarDismissed -> {
                clearSnackbar()
            }
        }
    }

    // Content Management Functions
    private fun loadVenues(isRefresh: Boolean) {
        if (isRefresh) {
            currentPage = 0
        }

        viewModelScope.launch {


            val response = repository.getVenues(
                filters = ExploreFilters(),
                page = currentPage
            )
            _uiState.value = _uiState.value.copy(
                contentState = _uiState.value.contentState.copy(
                    venues = response,
                    hasMorePages = true
                )
            )

        }
    }

    private fun loadMoreVenues() {
        if (isLoadingMore) return

        isLoadingMore = true
        viewModelScope.launch {
            try {
                loadVenues(isRefresh = false)
            } finally {
                isLoadingMore = false
            }
        }
    }

    // Filter Management Functions
    private fun showBottomSheet(type: BottomSheetType) {
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(
                isBottomSheetVisible = true,
                bottomSheetType = type,
                tempFilters = _uiState.value.filterState.filters.copy() // Initialize temp filters
            )
        )
    }

    private fun hideBottomSheet() {
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(
                isBottomSheetVisible = false,
                bottomSheetType = BottomSheetType.None
            )
        )
    }

    private fun updateTempFilters(filters: ExploreFiltersUiModel) {
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(
                tempFilters = filters
            )
        )
    }

    private fun applyTempFilters() {
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(
                filters = _uiState.value.filterState.tempFilters.copy(),
                isBottomSheetVisible = false,
                bottomSheetType = BottomSheetType.None
            )
        )
        loadVenues(isRefresh = true)
    }

    private fun clearAllFilters() {
        val clearedFilters = ExploreFiltersUiModel()
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(
                filters = clearedFilters,
                tempFilters = clearedFilters,
                isBottomSheetVisible = false,
                bottomSheetType = BottomSheetType.None
            )
        )
        loadVenues(isRefresh = true)
    }

    private fun updateSortOption(option: SortOption) {
        val updatedFilters = _uiState.value.filterState.filters.copy(sortOption = option)
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(filters = updatedFilters)
        )
        loadVenues(isRefresh = true)
    }

    private fun updateVenueType(type: VenueType) {
        val updatedFilters = _uiState.value.filterState.filters.copy(venueType = type)
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(filters = updatedFilters)
        )
        loadVenues(isRefresh = true)
    }

    private fun updatePriceRange(minPrice: Int, maxPrice: Int) {
        val updatedFilters = _uiState.value.filterState.filters.copy(
            priceRange = PriceRangeUiModel(min = minPrice, max = maxPrice)
        )
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(filters = updatedFilters)
        )
        loadVenues(isRefresh = true)
    }

    private fun updateSearchQuery(query: String) {
        val updatedFilters = _uiState.value.filterState.filters.copy(searchQuery = query)
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(filters = updatedFilters)
        )
        loadVenues(isRefresh = true)
    }

    private fun updateSelectedCity(city: String) {
        val updatedFilters = _uiState.value.filterState.filters.copy(selectedCity = city)
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(filters = updatedFilters)
        )
        loadVenues(isRefresh = true)
    }

    // Venue Interaction Functions
    private fun navigateToVenueDetail(venueId: String) {
        _uiState.value = _uiState.value.copy(
            navigationState = _uiState.value.navigationState.copy(
                navigateToVenueDetail = venueId
            )
        )
    }

    private fun toggleVenueBookmark(venueId: String) {
        val updatedVenues = _uiState.value.contentState.venues.map { venue ->
            if (venue.id == venueId) {
                venue.copy(isBookmarkSaved = !venue.isBookmarkSaved)
            } else {
                venue
            }
        }

        _uiState.value = _uiState.value.copy(
            contentState = _uiState.value.contentState.copy(venues = updatedVenues)
        )

        // TODO: Update bookmark in repository
        showSnackbar("Bookmark ${if (updatedVenues.find { it.id == venueId }?.isBookmarkSaved == true) "added" else "removed"}")
    }

    // Navigation Functions
    private fun navigateToMap() {
        _uiState.value = _uiState.value.copy(
            navigationState = _uiState.value.navigationState.copy(navigateToMap = true)
        )
    }

    private fun navigateToAdvancedSearch() {
        _uiState.value = _uiState.value.copy(
            navigationState = _uiState.value.navigationState.copy(navigateToAdvancedSearch = true)
        )
    }

    private fun clearNavigationState(resetType: ExploreContract.NavigationReset) {
        _uiState.value = _uiState.value.copy(
            navigationState = when (resetType) {
                ExploreContract.NavigationReset.VENUE_DETAIL -> {
                    _uiState.value.navigationState.copy(navigateToVenueDetail = null)
                }

                ExploreContract.NavigationReset.MAP -> {
                    _uiState.value.navigationState.copy(navigateToMap = false)
                }

                ExploreContract.NavigationReset.ADVANCED_SEARCH -> {
                    _uiState.value.navigationState.copy(navigateToAdvancedSearch = false)
                }
            }
        )
    }

    // Permission Functions
    private fun updateLocationPermissionGranted(granted: Boolean) {
        _uiState.value = _uiState.value.copy(
            interactionState = _uiState.value.interactionState.copy(
                isLocationPermissionGranted = granted
            )
        )
    }

    // Error & Message Functions
    private fun showError(error: ExploreError) {
        _uiState.value = _uiState.value.copy(
            contentState = _uiState.value.contentState.copy(error = error)
        )
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(
            contentState = _uiState.value.contentState.copy(error = null)
        )
    }

    private fun showSnackbar(message: String) {
        _uiState.value = _uiState.value.copy(
            navigationState = _uiState.value.navigationState.copy(snackbarMessage = message)
        )
    }

    private fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(
            navigationState = _uiState.value.navigationState.copy(snackbarMessage = null)
        )
    }
}
