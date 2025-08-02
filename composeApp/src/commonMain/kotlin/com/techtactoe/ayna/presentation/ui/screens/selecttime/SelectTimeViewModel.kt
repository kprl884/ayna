package com.techtactoe.ayna.presentation.ui.screens.selecttime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.domain.model.TimeSlot
import com.techtactoe.ayna.domain.usecase.CreateAppointmentUseCase
import com.techtactoe.ayna.domain.usecase.GetAvailableTimeSlotsUseCase
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.Job
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
    private val getAvailableTimeSlotsUseCase: GetAvailableTimeSlotsUseCase,
    private val createAppointmentUseCase: CreateAppointmentUseCase
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
        val nextAvailableDate: String = "",
        val isCreatingAppointment: Boolean = false,
        val appointmentCreated: String? = null // Contains appointment ID when created
    )

    private val _uiState = MutableStateFlow(SelectTimeUiState())
    val uiState: StateFlow<SelectTimeUiState> = _uiState.asStateFlow()

    // Parameters passed from navigation
    private var salonId: String = ""
    private var serviceId: String = ""

    // Job for managing loading operations
    private var currentLoadingJob: Job? = null

    fun initialize(salonId: String, serviceId: String) {
        this.salonId = salonId
        this.serviceId = serviceId
        loadTimeSlots(_uiState.value.selectedDate)
    }

    /**
     * Load available time slots for the selected date
     */
    fun loadTimeSlots(date: Long) {
        // Cancel any previous loading job
        currentLoadingJob?.cancel()

        currentLoadingJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedDate = date,
                isLoading = true,
                error = null,
                selectedTimeSlot = null // Clear selection when changing dates
            )

            getAvailableTimeSlotsUseCase(salonId, serviceId, date).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading to true above
                    }

                    is Resource.Success -> {
                        val slots = result.data ?: emptyList()
                        val isFullyBooked = slots.isEmpty()

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            availableSlots = slots,
                            isFullyBooked = isFullyBooked,
                            nextAvailableDate = if (isFullyBooked) getNextAvailableDate(date) else "",
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
     * Create appointment with selected time slot
     */
    fun createAppointment(salonName: String, serviceName: String) {
        val selectedSlot = _uiState.value.selectedTimeSlot ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreatingAppointment = true, error = null)

            val appointment = Appointment(
                id = "", // Will be generated by repository
                salonId = salonId,
                salonName = salonName,
                serviceName = serviceName,
                employeeId = "default", // Could be selected by user in future
                employeeName = "Available Staff",
                appointmentDateTime = selectedSlot.dateTime,
                status = AppointmentStatus.COMPLETED,
                price = 50.0, // Could be passed from service selection
                durationInMinutes = 60,
                notes = ""
            )

            createAppointmentUseCase(appointment).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading above
                    }
                    is Resource.Success -> {
                        val appointmentId = "${Clock.System.now().toEpochMilliseconds()}" // Mock ID generation
                        _uiState.value = _uiState.value.copy(
                            isCreatingAppointment = false,
                            appointmentCreated = appointmentId
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isCreatingAppointment = false,
                            error = result.message
                        )
                    }
                }
            }
        }
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
                    isSelected = dateTimeInMillis == selectedDate,
                    isDisabled = currentDate.dayOfWeek == DayOfWeek.SUNDAY
                )
            )
        }

        return dateOptions
    }

    /**
     * Get formatted next available date (skipping Sundays)
     */
    private fun getNextAvailableDate(currentDate: Long): String {
        var nextDate = Instant.fromEpochMilliseconds(currentDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date

        // Find next available date (skip Sunday)
        do {
            nextDate = nextDate.plus(1, DateTimeUnit.DAY)
        } while (nextDate.dayOfWeek == DayOfWeek.SUNDAY)

        val dayOfWeekText = when (nextDate.dayOfWeek) {
            DayOfWeek.MONDAY -> "Mon"
            DayOfWeek.TUESDAY -> "Tue"
            DayOfWeek.WEDNESDAY -> "Wed"
            DayOfWeek.THURSDAY -> "Thu"
            DayOfWeek.FRIDAY -> "Fri"
            DayOfWeek.SATURDAY -> "Sat"
            else -> ""
        }

        val monthText = when (nextDate.month) {
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
            else -> ""
        }

        return "$dayOfWeekText, $monthText ${nextDate.dayOfMonth}"
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
