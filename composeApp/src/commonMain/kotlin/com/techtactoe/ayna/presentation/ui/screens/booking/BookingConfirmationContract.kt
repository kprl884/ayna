package com.techtactoe.ayna.presentation.ui.screens.booking

/**
 * Contract defining the UI state and events for the BookingConfirmation screen
 * Following the standardized MVVM pattern
 */
interface BookingConfirmationContract {
    
    /**
     * Single source of truth for all UI state in the BookingConfirmation screen
     */
    data class UiState(
        // Data
        val appointmentId: String = "",
        val salonName: String = "",
        val serviceName: String = "",
        val appointmentDateTime: String = "",
        val price: String = "",
        
        // Loading states
        val isLoading: Boolean = false,
        
        // Success state
        val isConfirmed: Boolean = true,
        
        // Error state
        val errorMessage: String? = null,
        
        // Navigation flags
        val navigateToAppointments: Boolean = false,
        val navigateToHome: Boolean = false
    )
    
    /**
     * All possible user interactions with the BookingConfirmation screen
     */
    sealed interface UiEvent {
        // Initialization
        data class OnInitialize(val appointmentId: String) : UiEvent
        
        // Action events
        data object OnGoToAppointmentsClick : UiEvent
        data object OnDoneClick : UiEvent
        data object OnShareAppointment : UiEvent
        
        // Navigation events
        data class OnNavigationHandled(val resetNavigation: NavigationReset) : UiEvent
        
        // Error handling
        data object OnClearError : UiEvent
    }
    
    /**
     * Navigation reset options
     */
    enum class NavigationReset {
        APPOINTMENTS,
        HOME
    }
}
