package com.techtactoe.ayna.presentation.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.LatLng
import com.techtactoe.ayna.domain.model.SalonMapCard
import com.techtactoe.ayna.domain.model.SalonMapPin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(MapContract.UiState())
    val uiState: StateFlow<MapContract.UiState> = _uiState.asStateFlow()
    
    private val _uiEffect = MutableSharedFlow<MapContract.UiEffect>()
    val uiEffect: SharedFlow<MapContract.UiEffect> = _uiEffect.asSharedFlow()
    
    // Mock salon data for Lakewood, California area
    private val mockSalons = listOf(
        SalonMapCard(
            id = "1",
            name = "Hair Avenue",
            address = "Lakewood, California",
            location = "Lakewood, California",
            rating = 4.7,
            reviewCount = 312,
            distance = "2 km",
            imageUrl = "https://cdn.builder.io/api/v1/image/assets%2F5b75d307a1554817a72557f83cf3c781%2F7e84350429ab463c8d2a9981fcc3cce8?format=webp&width=800"
        ),
        SalonMapCard(
            id = "2",
            name = "House of Beauty",
            address = "Lakewood, California",
            location = "Lakewood, California",
            rating = 4.8,
            reviewCount = 256,
            distance = "1.5 km",
            imageUrl = "https://cdn.builder.io/api/v1/image/assets%2F5b75d307a1554817a72557f83cf3c781%2F3f2f1b7f03d446b282826a315397cfd8?format=webp&width=800"
        ),
        SalonMapCard(
            id = "3",
            name = "Salon by D",
            address = "Lakewood, California",
            location = "Lakewood, California",
            rating = 4.6,
            reviewCount = 189,
            distance = "3 km",
            imageUrl = "https://cdn.builder.io/api/v1/image/assets%2F5b75d307a1554817a72557f83cf3c781%2F1c64e8e562544033993378778cf275e2?format=webp&width=800"
        ),
        SalonMapCard(
            id = "4",
            name = "Beauty Studio",
            address = "Lakewood, California",
            location = "Lakewood, California",
            rating = 4.9,
            reviewCount = 421,
            distance = "1 km",
            imageUrl = "https://cdn.builder.io/api/v1/image/assets%2F5b75d307a1554817a72557f83cf3c781%2F7e84350429ab463c8d2a9981fcc3cce8?format=webp&width=800"
        ),
        SalonMapCard(
            id = "5",
            name = "Elite Hair",
            address = "Lakewood, California",
            location = "Lakewood, California",
            rating = 4.5,
            reviewCount = 167,
            distance = "2.5 km",
            imageUrl = "https://cdn.builder.io/api/v1/image/assets%2F5b75d307a1554817a72557f83cf3c781%2F3f2f1b7f03d446b282826a315397cfd8?format=webp&width=800"
        ),
        SalonMapCard(
            id = "6",
            name = "Style Studio",
            address = "Lakewood, California",
            location = "Lakewood, California",
            rating = 4.4,
            reviewCount = 298,
            distance = "3.5 km",
            imageUrl = "https://cdn.builder.io/api/v1/image/assets%2F5b75d307a1554817a72557f83cf3c781%2F1c64e8e562544033993378778cf275e2?format=webp&width=800"
        )
    )
    
    // Mock pin positions around Lakewood, California
    private val mockPins = listOf(
        SalonMapPin("1", LatLng(33.8536, -118.1339)),
        SalonMapPin("2", LatLng(33.8456, -118.1439)),
        SalonMapPin("3", LatLng(33.8636, -118.1239)),
        SalonMapPin("4", LatLng(33.8356, -118.1539)),
        SalonMapPin("5", LatLng(33.8736, -118.1139)),
        SalonMapPin("6", LatLng(33.8256, -118.1639))
    )
    
    init {
        loadSalons()
    }
    
    fun onEvent(event: MapContract.UiEvent) {
        when (event) {
            is MapContract.UiEvent.OnBackClick -> {
                viewModelScope.launch {
                    _uiEffect.emit(MapContract.UiEffect.NavigateBack)
                }
            }
            
            is MapContract.UiEvent.OnFilterClick -> {
                _uiState.value = _uiState.value.copy(isFilterVisible = true)
            }
            
            is MapContract.UiEvent.OnFilterDismiss -> {
                _uiState.value = _uiState.value.copy(isFilterVisible = false)
            }
            
            is MapContract.UiEvent.OnApplyFilter -> {
                _uiState.value = _uiState.value.copy(isFilterVisible = false)
                applySorting()
            }
            
            is MapContract.UiEvent.OnSalonCardDismiss -> {
                _uiState.value = _uiState.value.copy(
                    selectedSalon = null,
                    salonPins = _uiState.value.salonPins.map { it.copy(isSelected = false) }
                )
            }
            
            is MapContract.UiEvent.OnSalonPinClick -> {
                val salon = mockSalons.find { it.id == event.salonId }
                _uiState.value = _uiState.value.copy(
                    selectedSalon = salon,
                    salonPins = _uiState.value.salonPins.map { 
                        it.copy(isSelected = it.salonId == event.salonId) 
                    }
                )
            }
            
            is MapContract.UiEvent.OnSalonCardClick -> {
                viewModelScope.launch {
                    _uiEffect.emit(MapContract.UiEffect.NavigateToSalonDetail(event.salonId))
                }
            }
            
            is MapContract.UiEvent.OnFilterChange -> {
                _uiState.value = _uiState.value.copy(filterState = event.filterState)
            }
        }
    }
    
    private fun loadSalons() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            salonPins = mockPins
        )
    }
    
    private fun applySorting() {
        // Here you would apply actual sorting logic based on filterState.sortBy
        // For now, just keeping the existing order
    }
}
