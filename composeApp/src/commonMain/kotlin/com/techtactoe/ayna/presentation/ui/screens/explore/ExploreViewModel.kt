package com.techtactoe.ayna.presentation.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.techtactoe.ayna.data.FakeExploreRepository
import com.techtactoe.ayna.domain.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Explore screen handling venue discovery and filtering
 */
class ExploreViewModel(
    private val repository: FakeExploreRepository = FakeExploreRepository()
) {
    private val _screenState = MutableStateFlow(
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
    val screenState: StateFlow<ExploreScreenState> = _screenState.asStateFlow()

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

fun sampleVenues(): List<Venue> {
    return listOf(
        Venue(
            id = "1",
            name = "Ayna Beauty Salon",
            rating = 4.8,
            reviewCount = 245,
            district = "Beyoğlu",
            city = "Istanbul",
            images = listOf("https://example.com/image1.jpg"),
            services = listOf(
                VenueService(
                    id = "s1",
                    name = "Haircut & Style",
                    price = 15000, // 150 TRY
                    duration = 60
                ),
                VenueService(
                    id = "s2",
                    name = "Hair Color",
                    price = 25000, // 250 TRY
                    duration = 120
                )
            ),
            venueType = VenueType.EVERYONE,
            location = VenueLocation(
                latitude = 41.0351,
                longitude = 28.9831,
                address = "Istiklal Caddesi, Beyoğlu"
            )
        ),
        Venue(
            id = "2",
            name = "Elite Hair Studio",
            rating = 4.6,
            reviewCount = 189,
            district = "Şişli",
            city = "Istanbul",
            images = listOf("https://example.com/image2.jpg"),
            services = listOf(
                VenueService(
                    id = "s3",
                    name = "Premium Cut",
                    price = 20000, // 200 TRY
                    duration = 75
                )
            ),
            venueType = VenueType.EVERYONE
        ),
        Venue(
            id = "3",
            name = "Men's Grooming Lounge",
            rating = 4.9,
            reviewCount = 312,
            district = "Kadıköy",
            city = "Istanbul",
            images = listOf("https://example.com/image3.jpg"),
            services = listOf(
                VenueService(
                    id = "s4",
                    name = "Beard Trim",
                    price = 8000, // 80 TRY
                    duration = 30
                )
            ),
            venueType = VenueType.MALE_ONLY
        )
    )
}
