package com.techtactoe.ayna.presentation.ui.screens.salon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.data.MockSalonDetailRepository
import com.techtactoe.ayna.presentation.ui.screens.salon.model.SalonDetailEffect
import com.techtactoe.ayna.util.LogLevel
import com.techtactoe.ayna.util.log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the SalonDetail screen following the golden standard MVVM pattern
 */
class SalonDetailViewModel(
    private val salonId: String?
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalonDetailContract.UiState())
    val uiState: StateFlow<SalonDetailContract.UiState> = _uiState.asStateFlow()


    private val _effect = Channel<SalonDetailEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        salonId?.let {
            loadSalonDetail(salonId)
        }
    }

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
                log(LogLevel.DEBUG, "alpstein", "Scroll state changed: ${event.showStickyTabBar}")
                _uiState.update { it.copy(showStickyTabBar = event.showStickyTabBar) }
            }

            is SalonDetailContract.UiEvent.OnBackClick -> {
                sendEffect(SalonDetailEffect.NavigateUp)
            }

            is SalonDetailContract.UiEvent.OnShareClick -> {
                val shareText = uiState.value.salonDetail?.let { salon ->
                    "Check out ${salon.name} on Ayna! ${salon.about.description}"
                } ?: "Check out this amazing salon on Ayna!"
                sendEffect(SalonDetailEffect.Share(shareText))
            }

            is SalonDetailContract.UiEvent.OnFavoriteClick -> {
                _uiState.update { it.copy(isFavorite = !it.isFavorite) }
                // TODO: Call favorite use case
            }

            is SalonDetailContract.UiEvent.OnBookNowClick -> {
                salonId?.let {
                    uiState.value.salonDetail?.services?.firstOrNull()?.let { firstService ->
                        sendEffect(SalonDetailEffect.NavigateToSelectTime(salonId, firstService.id))
                    }
                }
            }

            is SalonDetailContract.UiEvent.OnServiceBookClick -> {
                salonId?.let {
                    sendEffect(SalonDetailEffect.NavigateToSelectTime(salonId, event.serviceId))
                }
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

    private fun sendEffect(effect: SalonDetailEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}