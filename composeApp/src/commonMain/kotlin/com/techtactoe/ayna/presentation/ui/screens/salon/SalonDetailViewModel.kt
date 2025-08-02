package com.techtactoe.ayna.presentation.ui.screens.salon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.data.MockSalonDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the SalonDetail screen following the golden standard MVVM pattern
 */
class SalonDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SalonDetailContract.UiState())
    val uiState: StateFlow<SalonDetailContract.UiState> = _uiState.asStateFlow()

    /**
     * Handle all user events from the UI
     */
    fun onEvent(event: SalonDetailContract.UiEvent) {
        when (event) {
            is SalonDetailContract.UiEvent.OnInitialize -> {
                _uiState.update { it.copy(salonId = event.salonId) }
                loadSalonDetail(event.salonId)
            }
            is SalonDetailContract.UiEvent.OnTabSelected -> {
                _uiState.update { it.copy(selectedTab = event.tab) }
            }
            is SalonDetailContract.UiEvent.OnScrollStateChanged -> {
                _uiState.update { it.copy(showStickyTabBar = event.showStickyTabBar) }
            }
            is SalonDetailContract.UiEvent.OnBackClick -> {
                // Handled in UI layer
            }
            is SalonDetailContract.UiEvent.OnShareClick -> {
                // Handled in UI layer
            }
            is SalonDetailContract.UiEvent.OnFavoriteClick -> {
                _uiState.update { it.copy(isFavorite = !it.isFavorite) }
                // TODO: Call favorite use case
            }
            is SalonDetailContract.UiEvent.OnBookNowClick -> {
                // Handled in UI layer
            }
            is SalonDetailContract.UiEvent.OnServiceBookClick -> {
                // Handled in UI layer
            }
            is SalonDetailContract.UiEvent.OnClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    /**
     * Load salon detail data
     */
    private fun loadSalonDetail(salonId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                val salonDetail = MockSalonDetailRepository.getSalonDetail(salonId)
                _uiState.update {
                    it.copy(
                        salonDetail = salonDetail,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load salon details"
                    )
                }
            }
        }
    }
}