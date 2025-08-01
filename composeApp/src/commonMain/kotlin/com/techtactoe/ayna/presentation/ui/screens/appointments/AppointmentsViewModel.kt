package com.techtactoe.ayna.presentation.ui.screens.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.domain.usecase.GetUserAppointmentsUseCase
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Appointments screen
 */
class AppointmentsViewModel(
    private val getUserAppointmentsUseCase: GetUserAppointmentsUseCase
) : ViewModel() {

    /**
     * Data class representing the UI state for the Appointments screen
     */
    data class AppointmentsUiState(
        val isLoading: Boolean = true,
        val appointments: List<Appointment> = emptyList(),
        val upcomingAppointments: List<Appointment> = emptyList(),
        val pastAppointments: List<Appointment> = emptyList(),
        val error: String? = null,
        val isEmpty: Boolean = false
    )

    private val _uiState = MutableStateFlow(AppointmentsUiState())
    val uiState: StateFlow<AppointmentsUiState> = _uiState.asStateFlow()

    // Current user ID (in real app, this would come from auth/session)
    private val currentUserId = "user123"

    init {
        loadAppointments()
    }

    /**
     * Load user appointments
     */
    fun loadAppointments() {
        viewModelScope.launch {
            getUserAppointmentsUseCase(currentUserId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        val appointments = result.data ?: emptyList()
                        val upcoming = appointments.filter { it.status == AppointmentStatus.UPCOMING }
                            .sortedBy { it.appointmentDateTime }
                        val past = appointments.filter { 
                            it.status == AppointmentStatus.COMPLETED || it.status == AppointmentStatus.CANCELLED 
                        }.sortedByDescending { it.appointmentDateTime }
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            appointments = appointments,
                            upcomingAppointments = upcoming,
                            pastAppointments = past,
                            isEmpty = appointments.isEmpty(),
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    /**
     * Refresh appointments
     */
    fun refreshAppointments() {
        loadAppointments()
    }

    /**
     * Clear any error messages
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
