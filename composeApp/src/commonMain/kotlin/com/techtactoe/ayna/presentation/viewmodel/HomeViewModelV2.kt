package com.techtactoe.ayna.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.SalonV2
import com.techtactoe.ayna.domain.usecase.GetRecommendedSalonsUseCase
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Enhanced ViewModel for the Home screen using Clean Architecture patterns
 */
class HomeViewModelV2(
    private val getRecommendedSalonsUseCase: GetRecommendedSalonsUseCase
) : ViewModel() {

    /**
     * Data class representing the UI state for the Home screen
     */
    data class HomeUiState(
        val isLoading: Boolean = false,
        val salons: List<SalonV2> = emptyList(),
        val error: String? = null,
        val isRefreshing: Boolean = false
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadRecommendedSalons()
    }

    /**
     * Load recommended salons from the repository
     */
    fun loadRecommendedSalons() {
        viewModelScope.launch {
            getRecommendedSalonsUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            salons = result.data ?: emptyList(),
                            error = null,
                            isRefreshing = false
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message,
                            isRefreshing = false
                        )
                    }
                }
            }
        }
    }

    /**
     * Refresh the recommended salons list
     */
    fun refreshSalons() {
        _uiState.value = _uiState.value.copy(isRefreshing = true)
        loadRecommendedSalons()
    }

    /**
     * Clear any error messages
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Handle salon selection
     */
    fun onSalonSelected(salon: SalonV2) {
        // Handle salon selection logic here
        // This could navigate to salon details or perform other actions
    }
}
