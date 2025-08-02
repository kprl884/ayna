package com.techtactoe.ayna.presentation.ui.screens.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.domain.usecase.GetUserAppointmentsUseCase
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Appointments screen following the golden standard MVVM pattern
 */
class AppointmentsViewModel(
    private val getUserAppointmentsUseCase: GetUserAppointmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentsContract.UiState())
    val uiState: StateFlow<AppointmentsContract.UiState> = _uiState.asStateFlow()

    // Current user ID (in real app, this would come from auth/session)
    private val currentUserId = "user123"

    init {
        loadAppointments()
    }

    /**
     * Handle all user events from the UI
     */
    fun onEvent(event: AppointmentsContract.UiEvent) {
        when (event) {
            is AppointmentsContract.UiEvent.OnInitialize -> {
                loadAppointments()
            }
            is AppointmentsContract.UiEvent.OnRefresh -> {
                refreshAppointments()
            }
            is AppointmentsContract.UiEvent.OnTabSelected -> {
                _uiState.update { it.copy(selectedTab = event.tab) }
            }
            is AppointmentsContract.UiEvent.OnAppointmentClicked -> {
                _uiState.update { it.copy(navigateToAppointmentDetail = event.appointmentId) }
            }
            is AppointmentsContract.UiEvent.OnNavigateToSearch -> {
                _uiState.update { it.copy(navigateToSearch = true) }
            }
            is AppointmentsContract.UiEvent.OnNavigationHandled -> {
                when (event.resetNavigation) {
                    AppointmentsContract.NavigationReset.SEARCH -> {
                        _uiState.update { it.copy(navigateToSearch = false) }
                    }
                    AppointmentsContract.NavigationReset.APPOINTMENT_DETAIL -> {
                        _uiState.update { it.copy(navigateToAppointmentDetail = null) }
                    }
                }
            }
            is AppointmentsContract.UiEvent.OnClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    /**
     * Load user appointments
     */
    private fun loadAppointments() {
        viewModelScope.launch {
            getUserAppointmentsUseCase(currentUserId).collect { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val appointments = result.data ?: emptyList()
                        val upcoming = appointments.filter { it.status == AppointmentStatus.UPCOMING }
                            .sortedBy { it.appointmentDateTime }
                        val past = appointments.filter {
                            it.status == AppointmentStatus.COMPLETED || it.status == AppointmentStatus.CANCELLED
                        }.sortedByDescending { it.appointmentDateTime }

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                appointments = appointments,
                                upcomingAppointments = upcoming,
                                pastAppointments = past,
                                isEmpty = appointments.isEmpty(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Refresh appointments
     */
    private fun refreshAppointments() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadAppointments()
    }
}
