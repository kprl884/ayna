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
                _uiState.update { it.copy(currentBottomSheet = BottomSheetType.None) }
            }
            is ExploreContract.UiEvent.OnUpdateTempFilters -> {
                _uiState.update { it.copy(tempFilters = event.filters) }
            }
            is ExploreContract.UiEvent.OnApplyTempFilters -> {
                val tempFilters = _uiState.value.tempFilters
                _uiState.update {
                    it.copy(
                        filters = tempFilters,
                        currentBottomSheet = BottomSheetType.None
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
            _screenState.update { currentState ->
                val currentUiState = currentState.uiState
                if (currentUiState is ExploreUiState.Success) {
                    currentState.copy(
                        uiState = currentUiState.copy(isLoading = true)
                    )
                } else {
                    currentState.copy(uiState = ExploreUiState.Loading)
                }
            }
        }

        val currentFilters = getCurrentFilters(_screenState.value.uiState)

        viewModelScope.launch {
            try {
                val result = repository.getVenues(currentFilters, currentPage)
                result.fold(
                    onSuccess = { venues ->
                        if (venues.isEmpty() && allVenues.isEmpty()) {
                            _screenState.update { currentState ->
                                currentState.copy(
                                    uiState = ExploreUiState.Empty(filters = currentFilters)
                                )
                            }
                        } else {
                            if (reset) {
                                allVenues.clear()
                            }
                            allVenues.addAll(venues)
                            currentPage++

                            _screenState.update { currentState ->
                                currentState.copy(
                                    uiState = ExploreUiState.Success(
                                        isLoading = false,
                                        venues = allVenues.toList(),
                                        hasMorePages = venues.isNotEmpty(),
                                        filters = currentFilters,
                                        isRefreshing = false
                                    )
                                )
                            }
                        }
                    },
                    onFailure = { exception ->
                        _screenState.update { currentState ->
                            currentState.copy(
                                uiState = ExploreUiState.Error(
                                    message = exception.message ?: "An error occurred",
                                    filters = currentFilters
                                )
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _screenState.update { currentState ->
                    currentState.copy(
                        uiState = ExploreUiState.Error(
                            message = e.message ?: "An error occurred",
                            filters = currentFilters
                        )
                    )
                }
            }
        }
    }

    private fun refreshVenues() {
        _screenState.update { screenState ->
            val currentUiState = screenState.uiState
            if (currentUiState is ExploreUiState.Success) {
                screenState.copy(
                    uiState = currentUiState.copy(isRefreshing = true, isLoading = false)
                )
            } else {
                screenState
            }
        }
        loadVenues(reset = true)
    }

    private fun updateFilters(filters: ExploreFilters) {
        _screenState.update { screenState ->
            when (val currentUiState = screenState.uiState) {
                is ExploreUiState.Success -> {
                    screenState.copy(
                        uiState = currentUiState.copy(filters = filters, isLoading = false)
                    )
                }

                is ExploreUiState.Error -> {
                    screenState.copy(
                        uiState = currentUiState.copy(filters = filters)
                    )
                }

                is ExploreUiState.Empty -> {
                    screenState.copy(
                        uiState = currentUiState.copy(filters = filters)
                    )
                }

                else -> {
                    // For loading state, just load with new filters
                    screenState
                }
            }
        }
        loadVenues(reset = true)
    }

    private fun updateSearchQuery(query: String) {
        val currentFilters = getCurrentFilters(_screenState.value.uiState)
        updateFilters(currentFilters.copy(searchQuery = query))
    }

    private fun updateSelectedCity(city: String) {
        val currentFilters = getCurrentFilters(_screenState.value.uiState)
        updateFilters(currentFilters.copy(selectedCity = city))
    }

    private fun clearFilters() {
        updateFilters(ExploreFilters())
    }

    private fun bookmarkVenue(venueId: String) {
        viewModelScope.launch {
            try {
                repository.bookmarkVenue(venueId)
                _events.emit(ExploreEvent.ShowSnackbar("Venue bookmarked"))
            } catch (e: Exception) {
                println("Failed to bookmark venue: $e")
                _events.emit(ExploreEvent.ShowSnackbar("Failed to bookmark venue"))
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
                    _screenState.update { currentState ->
                        when (val uiState = currentState.uiState) {
                            is ExploreUiState.Success -> {
                                currentState.copy(
                                    uiState = uiState.copy(isLocationPermissionGranted = true)
                                )
                            }

                            else -> currentState
                        }
                    }
                    _events.emit(ExploreEvent.ShowSnackbar("Location access granted"))
                    // Reload venues with location-based results
                    loadVenues(reset = true)
                } else {
                    _events.emit(ExploreEvent.ShowSnackbar("Location permission denied. Showing general results."))
                }
            } catch (e: Exception) {
                println("Failed to request location permission: ${e.message}")
                _events.emit(ExploreEvent.ShowSnackbar("Failed to request location permission"))
            }
        }
    }

    private fun getCurrentFilters(uiState: ExploreUiState): ExploreFilters {
        return when (uiState) {
            is ExploreUiState.Success -> uiState.filters
            is ExploreUiState.Error -> uiState.filters
            is ExploreUiState.Empty -> uiState.filters
            else -> ExploreFilters()
        }
    }
}
