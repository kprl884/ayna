package com.techtactoe.ayna.presentation.ui.screens.appointments

import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.presentation.ui.screens.appointments.model.AppointmentTab

/**
 * Contract defining the UI state and events for the Appointments screen
 * Following the standardized MVVM pattern
 */
interface AppointmentsContract {

    /**
     * Single source of truth for all UI state in the Appointments screen
     */
    data class UiState(
        // Data lists
        val appointments: List<Appointment> = emptyList(),
        val upcomingAppointments: List<Appointment> = emptyList(),
        val pastAppointments: List<Appointment> = emptyList(),

        // Loading states
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,

        // Tab state
        val selectedTab: AppointmentTab = AppointmentTab.UPCOMING,

        // UI states
        val isEmpty: Boolean = true,
        val errorMessage: String? = null,

        // Navigation flags
        val navigateToSearch: Boolean = false,
        val navigateToAppointmentDetail: String? = null
    )

    /**
     * All possible user interactions with the Appointments screen
     */
    sealed interface UiEvent {
        // Data events
        data object OnRefresh : UiEvent
        data object OnInitialize : UiEvent

        // Tab events
        data class OnTabSelected(val tab: AppointmentTab) : UiEvent

        // Appointment interaction events
        data class OnAppointmentClicked(val appointmentId: String) : UiEvent

        // Navigation events
        data object OnNavigateToSearch : UiEvent

        // Error handling
        data object OnClearError : UiEvent
        data object OnClearNavigateState : UiEvent
    }
}
