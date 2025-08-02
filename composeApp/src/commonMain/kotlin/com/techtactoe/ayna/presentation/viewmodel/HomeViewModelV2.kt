package com.techtactoe.ayna.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.usecase.GetRecommendedSalonsUseCase
import com.techtactoe.ayna.domain.util.Resource
import com.techtactoe.ayna.presentation.ui.screens.home.HomeContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen following the golden standard MVVM pattern
 */
class HomeViewModelV2(
    private val getRecommendedSalonsUseCase: GetRecommendedSalonsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeContract.UiState())
    val uiState: StateFlow<HomeContract.UiState> = _uiState.asStateFlow()

    init {
        loadRecommendedSalons()
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
            is HomeContract.UiEvent.OnSearchClick -> {
                _uiState.update { it.copy(navigateToSearch = true) }
            }
            is HomeContract.UiEvent.OnSalonClick -> {
                _uiState.update { it.copy(navigateToSalonDetail = event.salonId) }
            }
            is HomeContract.UiEvent.OnProfileClick -> {
                _uiState.update { it.copy(navigateToProfile = true) }
            }
            is HomeContract.UiEvent.OnNavigationHandled -> {
                when (event.resetNavigation) {
                    HomeContract.NavigationReset.SALON_DETAIL -> {
                        _uiState.update { it.copy(navigateToSalonDetail = null) }
                    }
                    HomeContract.NavigationReset.SEARCH -> {
                        _uiState.update { it.copy(navigateToSearch = false) }
                    }
                    HomeContract.NavigationReset.PROFILE -> {
                        _uiState.update { it.copy(navigateToProfile = false) }
                    }
                }
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
     * Refresh salons
     */
    private fun refreshSalons() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadRecommendedSalons()
    }
}
