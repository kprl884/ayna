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
     * Handles user intents and updates the state accordingly
     */
    fun handleIntent(intent: ExploreIntent) {
        when (intent) {
            is ExploreIntent.LoadVenues -> loadVenues(reset = true)
            is ExploreIntent.RefreshVenues -> refreshVenues()
            is ExploreIntent.LoadMoreVenues -> loadVenues(reset = false)
            is ExploreIntent.UpdateFilters -> updateFilters(intent.filters)
            is ExploreIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is ExploreIntent.UpdateSelectedCity -> updateSelectedCity(intent.city)
            is ExploreIntent.ClearFilters -> clearFilters()
            is ExploreIntent.BookmarkVenue -> bookmarkVenue(intent.venueId)
            is ExploreIntent.RequestLocationPermission -> requestLocationPermission()
        }
    }

    /**
     * Shows bottom sheet with specified type
     */
    fun showBottomSheet(type: BottomSheetType) {
        _screenState.update { currentState ->
            currentState.copy(
                currentBottomSheet = type,
                tempFilters = getCurrentFilters(currentState.uiState)
            )
        }
    }

    /**
     * Hides currently shown bottom sheet
     */
    fun hideBottomSheet() {
        _screenState.update { currentState ->
            currentState.copy(currentBottomSheet = BottomSheetType.None)
        }
    }

    /**
     * Updates temporary filters for bottom sheet
     */
    fun updateTempFilters(filters: ExploreFilters) {
        _screenState.update { currentState ->
            currentState.copy(tempFilters = filters)
        }
    }

    /**
     * Applies temporary filters to actual filters
     */
    fun applyTempFilters() {
        updateFilters(_screenState.value.tempFilters)
        hideBottomSheet()
    }

    /**
     * Navigates to venue detail screen
     */
    fun navigateToVenueDetail(venueId: String) {
        viewModelScope.launch {
            _events.emit(ExploreEvent.NavigateToVenueDetail(venueId))
        }
    }

    /**
     * Navigates to map screen
     */
    fun navigateToMap() {
        viewModelScope.launch {
            _events.emit(ExploreEvent.NavigateToMap)
        }
    }

    /**
     * Navigates to advanced search screen
     */
    fun navigateToAdvancedSearch() {
        viewModelScope.launch {
            _events.emit(ExploreEvent.NavigateToAdvancedSearch)
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
