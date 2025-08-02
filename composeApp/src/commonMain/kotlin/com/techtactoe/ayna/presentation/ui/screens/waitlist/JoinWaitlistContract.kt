package com.techtactoe.ayna.presentation.ui.screens.waitlist

import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime

/**
 * Contract defining the UI state and events for the JoinWaitlist screen
 * Following the standardized MVVM pattern
 */
interface JoinWaitlistContract {

    /**
     * Single source of truth for all UI state in the JoinWaitlist screen
     */
    data class UiState(
        // Form data
        val selectedDate: Long = Clock.System.now().toEpochMilliseconds(),
        val selectedTimeRange: String = "Any time",
        val timeRangeOptions: List<String> = listOf("Any time", "Morning", "Afternoon", "Evening"),

        // Loading states
        val isLoading: Boolean = false,
        val isSubmitting: Boolean = false,

        // Success states
        val isSuccess: Boolean = false,
        val hasAvailableSlots: Boolean = false,
        val availableSlotsMessage: String = "",

        // Error state
        val errorMessage: String? = null,

        // Navigation flags
        val navigateToBooking: Boolean = false,
        val navigateToSelectTime: Boolean = false,
        val navigateBack: Boolean = false,
        val navigateToClose: Boolean = false
    ) {
        val formattedDate: String
            get() {
                val selectedDate = kotlinx.datetime.Instant.fromEpochMilliseconds(selectedDate)
                    .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date

                val dayOfWeek = when (selectedDate.dayOfWeek) {
                    kotlinx.datetime.DayOfWeek.MONDAY -> "Mon"
                    kotlinx.datetime.DayOfWeek.TUESDAY -> "Tue"
                    kotlinx.datetime.DayOfWeek.WEDNESDAY -> "Wed"
                    kotlinx.datetime.DayOfWeek.THURSDAY -> "Thu"
                    kotlinx.datetime.DayOfWeek.FRIDAY -> "Fri"
                    kotlinx.datetime.DayOfWeek.SATURDAY -> "Sat"
                    kotlinx.datetime.DayOfWeek.SUNDAY -> "Sun"
                    else -> {}
                }

                val month = when (selectedDate.month) {
                    kotlinx.datetime.Month.JANUARY -> "Jan"
                    kotlinx.datetime.Month.FEBRUARY -> "Feb"
                    kotlinx.datetime.Month.MARCH -> "Mar"
                    kotlinx.datetime.Month.APRIL -> "Apr"
                    kotlinx.datetime.Month.MAY -> "May"
                    kotlinx.datetime.Month.JUNE -> "Jun"
                    kotlinx.datetime.Month.JULY -> "Jul"
                    kotlinx.datetime.Month.AUGUST -> "Aug"
                    kotlinx.datetime.Month.SEPTEMBER -> "Sep"
                    kotlinx.datetime.Month.OCTOBER -> "Oct"
                    kotlinx.datetime.Month.NOVEMBER -> "Nov"
                    kotlinx.datetime.Month.DECEMBER -> "Dec"
                    else -> {}
                }

                return "$dayOfWeek, $month ${selectedDate.dayOfMonth}"
            }
    }

    /**
     * All possible user interactions with the JoinWaitlist screen
     */
    sealed interface UiEvent {
        // Initialization
        data class OnInitialize(val salonId: String, val serviceId: String) : UiEvent

        // Form events
        data class OnDateSelected(val date: Long) : UiEvent
        data class OnTimeRangeSelected(val timeRange: String) : UiEvent
        data object OnAddAnotherTime : UiEvent

        // Action events
        data object OnJoinWaitlist : UiEvent
        data object OnBookNow : UiEvent
        data object OnSeeAvailableTimes : UiEvent

        // Navigation events
        data object OnBackClick : UiEvent
        data object OnCloseClick : UiEvent
        data class OnNavigationHandled(val resetNavigation: NavigationReset) : UiEvent

        // Error handling
        data object OnClearError : UiEvent
    }

    /**
     * Navigation reset options
     */
    enum class NavigationReset {
        BOOKING,
        SELECT_TIME,
        BACK,
        CLOSE
    }
}
