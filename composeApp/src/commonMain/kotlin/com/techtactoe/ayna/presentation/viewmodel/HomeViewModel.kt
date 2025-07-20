package com.techtactoe.ayna.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.usecase.GetNearbySalonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNearbySalonsUseCase: GetNearbySalonsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    init {
        loadSalons()
    }

    fun loadSalons() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        // Use the lifecycle-aware viewModelScope
        viewModelScope.launch {
            try {
                val result = getNearbySalonsUseCase()
                result.fold(
                    onSuccess = { salons ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            salons = salons,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            salons = emptyList(),
                            error = exception.message ?: "Bilinmeyen bir hata oluştu"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    salons = emptyList(),
                    error = e.message ?: "Bilinmeyen bir hata oluştu"
                )
            }
        }
    }
}