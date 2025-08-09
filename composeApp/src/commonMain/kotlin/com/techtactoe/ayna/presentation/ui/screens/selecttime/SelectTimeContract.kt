package com.techtactoe.ayna.presentation.ui.screens.selecttime

import com.techtactoe.ayna.domain.model.TimeSlot
import kotlin.time.ExperimentalTime

/**
 * Contract defining the UI state and events for the SelectTime screen
 * Following the standardized MVVM pattern
 */
interface SelectTimeContract {
    
    /**
     * Single source of truth for all UI state in the SelectTime screen
     */
    data class UiState @OptIn(ExperimentalTime::class) constructor(
        // Time slot data
        val availableSlots: List<TimeSlot> = emptyList(),
        val selectedTimeSlot: TimeSlot? = null,
        val selectedDate: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
        
        // Date options for selector
        val dateOptions: List<DateOption> = emptyList(),
        
        // Loading states
        val isLoading: Boolean = true,
        val isCreatingAppointment: Boolean = false,
        
        // Fully booked state
        val isFullyBooked: Boolean = false,
        val nextAvailableDate: String = "",
        
        // Navigation and success states
        val appointmentCreated: String? = null, // Contains appointment ID when created
        val navigateToWaitlist: Boolean = false,
        
        // Error handling
        val errorMessage: String? = null,
        
        // Form data
        val salonId: String = "",
        val serviceId: String = "",
        val salonName: String = "",
        val serviceName: String = ""
    ) {
        val canCreateAppointment: Boolean
            get() = selectedTimeSlot != null && !isCreatingAppointment && !isLoading
    }
    
    /**
     * All possible user interactions with the SelectTime screen
     */
    sealed interface UiEvent {
        // Initialization
        data class OnInitialize(val salonId: String, val serviceId: String) : UiEvent
        
        // Date selection
        data class OnDateSelected(val date: Long) : UiEvent
        data object OnGoToNextAvailableDate : UiEvent
        
        // Time slot selection
        data class OnTimeSlotSelected(val timeSlot: TimeSlot) : UiEvent
        
        // Appointment creation
        data class OnCreateAppointment(val salonName: String, val serviceName: String) : UiEvent
        
        // Navigation events
        data object OnJoinWaitlistClicked : UiEvent
        data object OnBackClicked : UiEvent
        data object OnCloseClicked : UiEvent
        data class OnNavigationHandled(val resetNavigation: NavigationReset) : UiEvent
        
        // Error handling
        data object OnClearError : UiEvent
        data object OnRetryLoadSlots : UiEvent
    }
    
    /**
     * Navigation reset options
     */
    enum class NavigationReset {
        APPOINTMENT_CREATED,
        WAITLIST
    }
}

/**
 * Data class for date selector options
 */
data class DateOption(
    val date: Long,
    val dayOfMonth: Int,
    val dayOfWeek: String,
    val isSelected: Boolean = false,
    val isDisabled: Boolean = false
)
