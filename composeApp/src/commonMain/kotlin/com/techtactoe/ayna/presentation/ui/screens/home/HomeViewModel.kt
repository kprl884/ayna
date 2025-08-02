package com.techtactoe.ayna.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.usecase.GetNearbySalonsUseCase
import com.techtactoe.ayna.domain.usecase.GetRecommendedSalonsUseCase
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen following the golden standard MVVM pattern
 */
class HomeViewModel(
    private val getRecommendedSalonsUseCase: GetRecommendedSalonsUseCase,
    private val getNearbySalonsUseCase: GetNearbySalonsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeContract.UiState())
    val uiState: StateFlow<HomeContract.UiState> = _uiState.asStateFlow()

    init {
        loadRecommendedSalons()
        loadSalons()
    }

    /**
     * Handle all user events from the UI
     */
    fun onEvent(event: HomeContract.UiEvent) {
        when (event) {
            is HomeContract.UiEvent.OnInitialize -> {
                loadRecommendedSalons()
            }

            is HomeContract.UiEvent.OnRefresh -> {
                refreshSalons()
            }

            is HomeContract.UiEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }

            // Navigation event'leri artık UI'da handle ediliyor
            is HomeContract.UiEvent.OnSearchClick,
            is HomeContract.UiEvent.OnSalonClick,
            is HomeContract.UiEvent.OnProfileClick -> {
                // Bu event'ler UI katmanında handle edilecek
                // ViewModel'de bir işlem yapmaya gerek yok
            }

            is HomeContract.UiEvent.OnClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    /**
     * Load recommended salons from the repository
     */
    private fun loadRecommendedSalons() {
        viewModelScope.launch {
            getRecommendedSalonsUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                salons = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Load nearby salons
     */
    private fun loadSalons() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val result = getNearbySalonsUseCase()
                result.fold(
                    onSuccess = { salons ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                salons = salons,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                salons = emptyList(),
                                errorMessage = exception.message ?: "Bilinmeyen bir hata oluştu"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        salons = emptyList(),
                        errorMessage = e.message ?: "Bilinmeyen bir hata oluştu"
                    )
                }
            }
        }
    }

    /**
     * Refresh salons
     */
    private fun refreshSalons() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadRecommendedSalons()
    }
}