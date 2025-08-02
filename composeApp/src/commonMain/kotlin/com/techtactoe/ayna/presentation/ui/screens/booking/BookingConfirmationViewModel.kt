package com.techtactoe.ayna.presentation.ui.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the BookingConfirmation screen following the golden standard MVVM pattern
 */
class BookingConfirmationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BookingConfirmationContract.UiState())
    val uiState: StateFlow<BookingConfirmationContract.UiState> = _uiState.asStateFlow()

    /**
     * Handle all user events from the UI
     */
    fun onEvent(event: BookingConfirmationContract.UiEvent) {
        when (event) {
            is BookingConfirmationContract.UiEvent.OnInitialize -> {
                loadAppointmentDetails(event.appointmentId)
            }
            is BookingConfirmationContract.UiEvent.OnGoToAppointmentsClick -> {
                _uiState.update { it.copy(navigateToAppointments = true) }
            }
            is BookingConfirmationContract.UiEvent.OnDoneClick -> {
                _uiState.update { it.copy(navigateToHome = true) }
            }
            is BookingConfirmationContract.UiEvent.OnShareAppointment -> {
                // TODO: Implement sharing functionality
            }
            is BookingConfirmationContract.UiEvent.OnNavigationHandled -> {
                when (event.resetNavigation) {
                    BookingConfirmationContract.NavigationReset.APPOINTMENTS -> {
                        _uiState.update { it.copy(navigateToAppointments = false) }
                    }
                    BookingConfirmationContract.NavigationReset.HOME -> {
                        _uiState.update { it.copy(navigateToHome = false) }
                    }
                }
            }
            is BookingConfirmationContract.UiEvent.OnClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    /**
     * Load appointment details for the confirmation
     */
    private fun loadAppointmentDetails(appointmentId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                
                // TODO: In real app, fetch appointment details from repository
                // For now, use mock data
                _uiState.update {
                    it.copy(
                        appointmentId = appointmentId,
                        salonName = "Beauty Salon",
                        serviceName = "Haircut & Styling",
                        appointmentDateTime = "Tomorrow at 2:00 PM",
                        price = "₺150",
                        isLoading = false,
                        isConfirmed = true,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load appointment details"
                    )
                }
            }
        }
    }
}
