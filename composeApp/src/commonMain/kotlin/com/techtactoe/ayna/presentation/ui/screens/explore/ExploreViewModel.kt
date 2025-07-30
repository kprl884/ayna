package com.techtactoe.ayna.presentation.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.techtactoe.ayna.data.MockVenueRepository
import com.techtactoe.ayna.domain.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Explore screen handling venue discovery and filtering
 */
class ExploreViewModel(
    private val repository: MockVenueRepository = MockVenueRepository()
) {
    var screenState by mutableStateOf(
        ExploreScreenState(
            uiState = ExploreUiState.Success(
                isLoading = false,
                venues = sampleVenues(),
                isRefreshing = false,
                hasMorePages = true,
                filters = ExploreFilters(),
                isLocationPermissionGranted = false
            )
        )
    )
        private set

    private val _events = MutableSharedFlow<ExploreEvent>()
    val events = _events.asSharedFlow()

    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private var currentPage = 0
    private val allVenues = mutableListOf<Venue>()
    
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
        screenState = screenState.copy(
            currentBottomSheet = type,
            tempFilters = getCurrentFilters()
        )
    }
    
    /**
     * Hides currently shown bottom sheet
     */
    fun hideBottomSheet() {
        screenState = screenState.copy(currentBottomSheet = BottomSheetType.None)
    }
    
    /**
     * Updates temporary filters for bottom sheet
     */
    fun updateTempFilters(filters: ExploreFilters) {
        screenState = screenState.copy(tempFilters = filters)
    }
    
    /**
     * Applies temporary filters to actual filters
     */
    fun applyTempFilters() {
        updateFilters(screenState.tempFilters)
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
            val currentState = screenState.uiState
            if (currentState is ExploreUiState.Success) {
                screenState = screenState.copy(
                    uiState = currentState.copy(isLoading = true)
                )
            } else {
                screenState = screenState.copy(uiState = ExploreUiState.Loading)
            }
        }
        
        val currentFilters = getCurrentFilters()
        
        viewModelScope.launch {
            try {
                val result = repository.getVenues(currentFilters, currentPage)
                result.fold(
                    onSuccess = { venues ->
                        if (venues.isEmpty() && allVenues.isEmpty()) {
                            screenState = screenState.copy(
                                uiState = ExploreUiState.Empty(filters = currentFilters)
                            )
                        } else {
                            if (reset) {
                                allVenues.clear()
                            }
                            allVenues.addAll(venues)
                            currentPage++
                            
                            screenState = screenState.copy(
                                uiState = ExploreUiState.Success(
                                    isLoading = false,
                                    venues = allVenues.toList(),
                                    hasMorePages = venues.isNotEmpty(),
                                    filters = currentFilters,
                                    isRefreshing = false
                                )
                            )
                        }
                    },
                    onFailure = { exception ->
                        screenState = screenState.copy(
                            uiState = ExploreUiState.Error(
                                message = exception.message ?: "An error occurred",
                                filters = currentFilters
                            )
                        )
                    }
                )
            } catch (e: Exception) {
                screenState = screenState.copy(
                    uiState = ExploreUiState.Error(
                        message = e.message ?: "An error occurred",
                        filters = currentFilters
                    )
                )
            }
        }
    }
    
    private fun refreshVenues() {
        val currentState = screenState.uiState
        if (currentState is ExploreUiState.Success) {
            screenState = screenState.copy(
                uiState = currentState.copy(isRefreshing = true, isLoading = false)
            )
        }
        loadVenues(reset = true)
    }
    
    private fun updateFilters(filters: ExploreFilters) {
        when (val currentState = screenState.uiState) {
            is ExploreUiState.Success -> {
                screenState = screenState.copy(
                    uiState = currentState.copy(filters = filters)
                )
            }
            is ExploreUiState.Error -> {
                screenState = screenState.copy(
                    uiState = currentState.copy(filters = filters)
                )
            }
            is ExploreUiState.Empty -> {
                screenState = screenState.copy(
                    uiState = currentState.copy(filters = filters)
                )
            }
            else -> {
                // For loading state, just load with new filters
            }
        }
        loadVenues(reset = true)
    }
    
    private fun updateSearchQuery(query: String) {
        val currentFilters = getCurrentFilters()
        updateFilters(currentFilters.copy(searchQuery = query))
    }
    
    private fun updateSelectedCity(city: String) {
        val currentFilters = getCurrentFilters()
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
                _events.emit(ExploreEvent.ShowSnackbar("Failed to bookmark venue"))
            }
        }
    }
    
    private fun requestLocationPermission() {
        // This would be implemented with actual location permission logic
        viewModelScope.launch {
            _events.emit(ExploreEvent.ShowSnackbar("Fresha doesn't have permission to access your location"))
        }
    }
    
    private fun getCurrentFilters(): ExploreFilters {
        return when (val currentState = screenState.uiState) {
            is ExploreUiState.Success -> currentState.filters
            is ExploreUiState.Error -> currentState.filters
            is ExploreUiState.Empty -> currentState.filters
            else -> ExploreFilters()
        }
    }
}
