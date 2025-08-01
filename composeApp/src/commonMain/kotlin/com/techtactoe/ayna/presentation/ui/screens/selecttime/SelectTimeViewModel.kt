package com.techtactoe.ayna.presentation.ui.screens.selecttime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.TimeSlot
import com.techtactoe.ayna.domain.usecase.GetAvailableTimeSlotsUseCase
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

/**
 * ViewModel for the Select Time screen
 */
class SelectTimeViewModel(
    private val getAvailableTimeSlotsUseCase: GetAvailableTimeSlotsUseCase
) : ViewModel() {

    /**
     * Data class representing the UI state for the Select Time screen
     */
    data class SelectTimeUiState(
        val isLoading: Boolean = true,
        val availableSlots: List<TimeSlot> = emptyList(),
        val selectedDate: Long = Clock.System.now().toEpochMilliseconds(),
        val selectedTimeSlot: TimeSlot? = null,
        val error: String? = null,
        val isFullyBooked: Boolean = false,
        val nextAvailableDate: String = ""
    )

    private val _uiState = MutableStateFlow(SelectTimeUiState())
    val uiState: StateFlow<SelectTimeUiState> = _uiState.asStateFlow()

    // Parameters passed from navigation
    private var salonId: String = ""
    private var serviceId: String = ""

    fun initialize(salonId: String, serviceId: String) {
        this.salonId = salonId
        this.serviceId = serviceId
        loadTimeSlots(_uiState.value.selectedDate)
    }

    /**
     * Load available time slots for the selected date
     */
    fun loadTimeSlots(date: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedDate = date,
                isLoading = true,
                error = null
            )

            getAvailableTimeSlotsUseCase(salonId, serviceId, date).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        val slots = result.data ?: emptyList()
                        val isFullyBooked = slots.isEmpty()

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            availableSlots = slots,
                            isFullyBooked = isFullyBooked,
                            nextAvailableDate = if (isFullyBooked) "Sat, Aug 2" else "",
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
     * Select a specific time slot
     */
    fun selectTimeSlot(timeSlot: TimeSlot) {
        _uiState.value = _uiState.value.copy(selectedTimeSlot = timeSlot)
    }

    /**
     * Go to the next available date
     */
    fun goToNextAvailableDate() {
        var currentDate = Instant.fromEpochMilliseconds(_uiState.value.selectedDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date

        // Find next available date (skip Sunday)
        do {
            currentDate = currentDate.plus(1, DateTimeUnit.DAY)
        } while (currentDate.dayOfWeek == DayOfWeek.SUNDAY)

        val nextDateTime = currentDate.atStartOfDayIn(TimeZone.currentSystemDefault())
        loadTimeSlots(nextDateTime.toEpochMilliseconds())
    }

    /**
     * Clear any error messages
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Get generated date options for the date selector
     */
    fun getDateOptions(selectedDate: Long = _uiState.value.selectedDate): List<DateOption> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dateOptions = mutableListOf<DateOption>()

        for (i in 0..6) { // Show 7 days
            val currentDate = today.plus(i, DateTimeUnit.DAY)
            val dayOfWeekText = when (currentDate.dayOfWeek) {
                DayOfWeek.MONDAY -> "Mon"
                DayOfWeek.TUESDAY -> "Tue"
                DayOfWeek.WEDNESDAY -> "Wed"
                DayOfWeek.THURSDAY -> "Thu"
                DayOfWeek.FRIDAY -> "Fri"
                DayOfWeek.SATURDAY -> "Sat"
                DayOfWeek.SUNDAY -> "Sun"
                else -> ""
            }

            val dateTimeInMillis =
                currentDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()

            dateOptions.add(
                DateOption(
                    date = dateTimeInMillis,
                    dayOfMonth = currentDate.dayOfMonth,
                    dayOfWeek = dayOfWeekText,
                    isSelected = dateTimeInMillis == _uiState.value.selectedDate,
                    isDisabled = currentDate.dayOfWeek == DayOfWeek.SUNDAY
                )
            )
        }

        return dateOptions
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
