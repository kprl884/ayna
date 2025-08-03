package com.techtactoe.ayna.presentation.ui.screens.map

import androidx.compose.runtime.Stable
import com.techtactoe.ayna.domain.model.LatLng
import com.techtactoe.ayna.domain.model.MapFilterState
import com.techtactoe.ayna.domain.model.SalonMapCard
import com.techtactoe.ayna.domain.model.SalonMapPin

interface MapContract {
    
    @Stable
    data class UiState(
        val isLoading: Boolean = false,
        val mapCenter: LatLng = LatLng(33.8536, -118.1339), // Lakewood, California
        val salonPins: List<SalonMapPin> = emptyList(),
        val selectedSalon: SalonMapCard? = null,
        val isFilterVisible: Boolean = false,
        val filterState: MapFilterState = MapFilterState(),
        val locationName: String = "Lakewood, California",
        val error: String? = null
    )
    
    sealed interface UiEvent {
        data object OnBackClick : UiEvent
        data object OnFilterClick : UiEvent
        data object OnFilterDismiss : UiEvent
        data object OnApplyFilter : UiEvent
        data object OnSalonCardDismiss : UiEvent
        data class OnSalonPinClick(val salonId: String) : UiEvent
        data class OnSalonCardClick(val salonId: String) : UiEvent
        data class OnFilterChange(val filterState: MapFilterState) : UiEvent
    }
    
    sealed interface UiEffect {
        data object NavigateBack : UiEffect
        data class NavigateToSalonDetail(val salonId: String) : UiEffect
    }
}
