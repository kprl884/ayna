package com.techtactoe.ayna.presentation.ui.screens.explore

import com.techtactoe.ayna.data.FakeExploreRepository
import com.techtactoe.ayna.domain.model.ExploreFilters
import com.techtactoe.ayna.domain.model.Venue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Explore screen following the standardized MVVM pattern
 * Single StateFlow for UI state and single onEvent function for all user interactions
 */
class ExploreViewModel(
    private val repository: FakeExploreRepository = FakeExploreRepository()
) {
    private val _uiState = MutableStateFlow(
        ExploreContract.UiState(
            venues = sampleVenues(),
            isSuccess = true
        )
    )
    val uiState: StateFlow<ExploreContract.UiState> = _uiState.asStateFlow()

    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private var currentPage = 0
    private val allVenues = mutableListOf<Venue>()

    init {
        loadVenues(reset = true)
    }

    /**
     * Single entry point for all user interactions with the Explore screen
     * Following the golden standard MVVM pattern
     */
    fun onEvent(event: ExploreContract.UiEvent) {
        when (event) {
            // Filter events
            is ExploreContract.UiEvent.OnShowBottomSheet -> {
                _uiState.update {
                    it.copy(
                        currentBottomSheet = event.type,
                        tempFilters = it.filters
                    )
                }
            }
            is ExploreContract.UiEvent.OnHideBottomSheet -> {
                _uiState.update { it.copy(currentBottomSheet = ExploreContract.BottomSheetType.None) }
            }
            is ExploreContract.UiEvent.OnUpdateTempFilters -> {
                _uiState.update { it.copy(tempFilters = event.filters) }
            }
            is ExploreContract.UiEvent.OnApplyTempFilters -> {
                val tempFilters = _uiState.value.tempFilters
                _uiState.update {
                    it.copy(
                        filters = tempFilters,
                        currentBottomSheet = ExploreContract.BottomSheetType.None
                    )
                }
                loadVenues(reset = true)
            }
            is ExploreContract.UiEvent.OnClearFilters -> {
                _uiState.update {
                    it.copy(
                        filters = ExploreFilters(),
                        tempFilters = ExploreFilters()
                    )
                }
                loadVenues(reset = true)
            }

            // Search events
            is ExploreContract.UiEvent.OnSearchQueryChanged -> {
                val newFilters = _uiState.value.filters.copy(searchQuery = event.query)
                _uiState.update {
                    it.copy(
                        filters = newFilters,
                        searchQuery = event.query
                    )
                }
                loadVenues(reset = true)
            }
            is ExploreContract.UiEvent.OnSelectedCityChanged -> {
                val newFilters = _uiState.value.filters.copy(selectedCity = event.city)
                _uiState.update {
                    it.copy(
                        filters = newFilters,
                        selectedCity = event.city
                    )
                }
                loadVenues(reset = true)
            }

            // Venue interaction events
            is ExploreContract.UiEvent.OnVenueClicked -> {
                _uiState.update { it.copy(navigateToVenueDetail = event.venueId) }
            }
            is ExploreContract.UiEvent.OnVenueBookmarked -> {
                bookmarkVenue(event.venueId)
            }

            // Navigation events
            is ExploreContract.UiEvent.OnNavigateToMap -> {
                _uiState.update { it.copy(navigateToMap = true) }
            }
            is ExploreContract.UiEvent.OnNavigateToAdvancedSearch -> {
                _uiState.update { it.copy(navigateToAdvancedSearch = true) }
            }
            is ExploreContract.UiEvent.OnNavigationHandled -> {
                when (event.resetNavigation) {
                    ExploreContract.NavigationReset.VENUE_DETAIL -> {
                        _uiState.update { it.copy(navigateToVenueDetail = null) }
                    }
                    ExploreContract.NavigationReset.MAP -> {
                        _uiState.update { it.copy(navigateToMap = false) }
                    }
                    ExploreContract.NavigationReset.ADVANCED_SEARCH -> {
                        _uiState.update { it.copy(navigateToAdvancedSearch = false) }
                    }
                }
            }

            // List events
            is ExploreContract.UiEvent.OnRefreshVenues -> {
                refreshVenues()
            }
            is ExploreContract.UiEvent.OnLoadMoreVenues -> {
                loadVenues(reset = false)
            }

            // Permission events
            is ExploreContract.UiEvent.OnRequestLocationPermission -> {
                requestLocationPermission()
            }

            // Error handling
            is ExploreContract.UiEvent.OnClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            is ExploreContract.UiEvent.OnSnackbarDismissed -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
        }
    }

    private fun loadVenues(reset: Boolean) {
        if (reset) {
            currentPage = 0
            allVenues.clear()
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        }

        val currentFilters = _uiState.value.filters

        viewModelScope.launch {
            try {
                val result = repository.getVenues(currentFilters, currentPage)
                result.fold(
                    onSuccess = { venues ->
                        if (reset) {
                            allVenues.clear()
                        }
                        allVenues.addAll(venues)
                        currentPage++

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                venues = allVenues.toList(),
                                hasMorePages = venues.isNotEmpty(),
                                isRefreshing = false,
                                isSuccess = true,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = exception.message ?: "An error occurred",
                                isRefreshing = false
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "An error occurred",
                        isRefreshing = false
                    )
                }
            }
        }
    }

    private fun refreshVenues() {
        _uiState.update { it.copy(isRefreshing = true, isLoading = false) }
        loadVenues(reset = true)
    }

    private fun bookmarkVenue(venueId: String) {
        viewModelScope.launch {
            try {
                repository.bookmarkVenue(venueId)
                _uiState.update { it.copy(snackbarMessage = "Venue bookmarked") }
            } catch (e: Exception) {
                println("Failed to bookmark venue: $e")
                _uiState.update { it.copy(snackbarMessage = "Failed to bookmark venue") }
            }
        }
    }

    private fun requestLocationPermission() {
        // This would be implemented with actual location permission logic
        // In a real app, this would call platform-specific permission request methods
        viewModelScope.launch {
            try {
                // Simulate permission request process
                val isGranted = true // Mock - would be actual permission result

                if (isGranted) {
                    // Update state to reflect permission granted
                    _uiState.update {
                        it.copy(
                            isLocationPermissionGranted = true,
                            snackbarMessage = "Location access granted"
                        )
                    }
                    // Reload venues with location-based results
                    loadVenues(reset = true)
                } else {
                    _uiState.update {
                        it.copy(snackbarMessage = "Location permission denied. Showing general results.")
                    }
                }
            } catch (e: Exception) {
                println("Failed to request location permission: ${e.message}")
                _uiState.update {
                    it.copy(snackbarMessage = "Failed to request location permission")
                }
            }
        }
    }
}
