package com.techtactoe.ayna.presentation.ui.screens.waitlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.WaitlistRequest
import com.techtactoe.ayna.domain.usecase.JoinWaitlistUseCase
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.ExperimentalTime

/**
 * ViewModel for the Join Waitlist screen following the golden standard MVVM pattern
 */
class JoinWaitlistViewModel(
    private val joinWaitlistUseCase: JoinWaitlistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinWaitlistContract.UiState())
    val uiState: StateFlow<JoinWaitlistContract.UiState> = _uiState.asStateFlow()

    // Parameters from navigation
    private var salonId: String = ""
    private var serviceId: String = ""
    private val currentUserId = "user123" // In real app, from auth

    /**
     * Handle all user events from the UI
     */
    fun onEvent(event: JoinWaitlistContract.UiEvent) {
        when (event) {
            is JoinWaitlistContract.UiEvent.OnInitialize -> {
                salonId = event.salonId
                serviceId = event.serviceId
                checkAvailableSlots()
            }
            is JoinWaitlistContract.UiEvent.OnDateSelected -> {
                _uiState.update { it.copy(selectedDate = event.date) }
                checkAvailableSlots()
            }
            is JoinWaitlistContract.UiEvent.OnTimeRangeSelected -> {
                _uiState.update { it.copy(selectedTimeRange = event.timeRange) }
            }
            is JoinWaitlistContract.UiEvent.OnAddAnotherTime -> {
                // TODO: Implement adding another time slot preference
            }
            is JoinWaitlistContract.UiEvent.OnJoinWaitlist -> {
                joinWaitlist()
            }
            is JoinWaitlistContract.UiEvent.OnBookNow -> {
                _uiState.update { it.copy(navigateToBooking = true) }
            }
            is JoinWaitlistContract.UiEvent.OnSeeAvailableTimes -> {
                _uiState.update { it.copy(navigateToSelectTime = true) }
            }
            is JoinWaitlistContract.UiEvent.OnBackClick -> {
                _uiState.update { it.copy(navigateBack = true) }
            }
            is JoinWaitlistContract.UiEvent.OnCloseClick -> {
                _uiState.update { it.copy(navigateToClose = true) }
            }
            is JoinWaitlistContract.UiEvent.OnNavigationHandled -> {
                when (event.resetNavigation) {
                    JoinWaitlistContract.NavigationReset.BOOKING -> {
                        _uiState.update { it.copy(navigateToBooking = false) }
                    }
                    JoinWaitlistContract.NavigationReset.SELECT_TIME -> {
                        _uiState.update { it.copy(navigateToSelectTime = false) }
                    }
                    JoinWaitlistContract.NavigationReset.BACK -> {
                        _uiState.update { it.copy(navigateBack = false) }
                    }
                    JoinWaitlistContract.NavigationReset.CLOSE -> {
                        _uiState.update { it.copy(navigateToClose = false) }
                    }
                }
            }
            is JoinWaitlistContract.UiEvent.OnClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    /**
     * Join the waitlist
     */
    private fun joinWaitlist() {
        viewModelScope.launch {
            val request = WaitlistRequest(
                userId = currentUserId,
                salonId = salonId,
                serviceId = serviceId,
                preferredDate = _uiState.value.selectedDate,
                preferredTimeRange = _uiState.value.selectedTimeRange
            )

            joinWaitlistUseCase(request).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isSubmitting = true,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                isSuccess = true,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                errorMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if there are available slots for the selected date
     */
    @OptIn(ExperimentalTime::class)
    private fun checkAvailableSlots() {
        // Simulate checking for available slots
        // In this example, we'll say there are available slots for Saturday
        val selectedDate = Instant.fromEpochMilliseconds(_uiState.value.selectedDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dayOfWeek = selectedDate.dayOfWeek

        if (dayOfWeek == DayOfWeek.SATURDAY) {
            _uiState.update {
                it.copy(
                    hasAvailableSlots = true,
                    availableSlotsMessage = "There are time slots available to book on the selected dates, adjust your selection or book now."
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    hasAvailableSlots = false,
                    availableSlotsMessage = ""
                )
            }
        }
    }
}
