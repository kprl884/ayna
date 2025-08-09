package com.techtactoe.ayna.presentation.ui.screens.selecttime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.domain.usecase.CreateAppointmentUseCase
import com.techtactoe.ayna.domain.usecase.GetAvailableTimeSlotsUseCase
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * ViewModel for the Select Time screen following the standardized MVVM pattern
 * Single StateFlow for UI state and single onEvent function for all user interactions
 */
class SelectTimeViewModel(
    private val getAvailableTimeSlotsUseCase: GetAvailableTimeSlotsUseCase,
    private val createAppointmentUseCase: CreateAppointmentUseCase,
    private val validateSlotSelectionUseCase: com.techtactoe.ayna.domain.usecase.ValidateSlotSelectionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectTimeContract.UiState())
    val uiState: StateFlow<SelectTimeContract.UiState> = _uiState.asStateFlow()

    // Job for managing loading operations
    private var currentLoadingJob: Job? = null

    /**
     * Single entry point for all user interactions with the SelectTime screen
     * Following the golden standard MVVM pattern
     */
    fun onEvent(event: SelectTimeContract.UiEvent) {
        when (event) {
            // Initialization
            is SelectTimeContract.UiEvent.OnInitialize -> {
                _uiState.update {
                    it.copy(
                        salonId = event.salonId,
                        serviceId = event.serviceId,
                        dateOptions = getDateOptions(it.selectedDate)
                    )
                }
                loadTimeSlots(_uiState.value.selectedDate)
            }

            // Date selection
            is SelectTimeContract.UiEvent.OnDateSelected -> {
                loadTimeSlots(event.date)
            }
            is SelectTimeContract.UiEvent.OnGoToNextAvailableDate -> {
                goToNextAvailableDate()
            }

            // Time slot selection
            is SelectTimeContract.UiEvent.OnTimeSlotSelected -> {
                _uiState.update { it.copy(selectedTimeSlot = event.timeSlot) }
            }

            // Appointment creation
            is SelectTimeContract.UiEvent.OnCreateAppointment -> {
                createAppointment(event.salonName, event.serviceName)
            }

            // Navigation events
            is SelectTimeContract.UiEvent.OnJoinWaitlistClicked -> {
                _uiState.update { it.copy(navigateToWaitlist = true) }
            }
            is SelectTimeContract.UiEvent.OnBackClicked -> {
                // Handle back navigation - typically handled by navigation component
            }
            is SelectTimeContract.UiEvent.OnCloseClicked -> {
                // Handle close navigation - typically handled by navigation component
            }
            is SelectTimeContract.UiEvent.OnNavigationHandled -> {
                when (event.resetNavigation) {
                    SelectTimeContract.NavigationReset.APPOINTMENT_CREATED -> {
                        _uiState.update { it.copy(appointmentCreated = null) }
                    }
                    SelectTimeContract.NavigationReset.WAITLIST -> {
                        _uiState.update { it.copy(navigateToWaitlist = false) }
                    }
                }
            }

            // Error handling
            is SelectTimeContract.UiEvent.OnClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            is SelectTimeContract.UiEvent.OnRetryLoadSlots -> {
                loadTimeSlots(_uiState.value.selectedDate)
            }
        }
    }

    /**
     * Load available time slots for the selected date
     */
    private fun loadTimeSlots(date: Long) {
        // Cancel any previous loading job
        currentLoadingJob?.cancel()

        currentLoadingJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedDate = date,
                    isLoading = true,
                    errorMessage = null,
                    selectedTimeSlot = null, // Clear selection when changing dates
                    dateOptions = getDateOptions(date)
                )
            }

            getAvailableTimeSlotsUseCase(_uiState.value.salonId, _uiState.value.serviceId, date).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading to true above
                    }

                    is Resource.Success -> {
                        val slots = result.data ?: emptyList()
                        val isFullyBooked = slots.isEmpty()

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                availableSlots = slots,
                                isFullyBooked = isFullyBooked,
                                nextAvailableDate = if (isFullyBooked) getNextAvailableDate(date) else "",
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
     * Create appointment with selected time slot
     */
    @OptIn(ExperimentalTime::class)
    fun createAppointment(salonName: String, serviceName: String) {
        val selectedSlot = _uiState.value.selectedTimeSlot
        when (val validation = validateSlotSelectionUseCase(selectedSlot)) {
            is com.techtactoe.ayna.domain.usecase.ValidateSlotSelectionUseCase.Result.Error -> {
                _uiState.update { it.copy(errorMessage = "Invalid or unavailable time slot.") }
                return
            }
            com.techtactoe.ayna.domain.usecase.ValidateSlotSelectionUseCase.Result.Ok -> { /* proceed */ }
        }
        val nonNullSlot = selectedSlot!!

        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingAppointment = true, errorMessage = null) }

            val generatedId = "apt_${kotlin.time.Clock.System.now().toEpochMilliseconds()}"
            val appointment = Appointment(
                id = generatedId,
                salonId = _uiState.value.salonId,
                salonName = salonName,
                serviceName = serviceName,
                employeeId = "default", // Could be selected by user in future
                employeeName = "Available Staff",
                appointmentDateTime = nonNullSlot.dateTime,
                status = AppointmentStatus.UPCOMING,
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
                        val appointmentId = "${kotlin.time.Clock.System.now().toEpochMilliseconds()}" // Mock ID generation
                        _uiState.update {
                            it.copy(
                                isCreatingAppointment = false,
                                appointmentCreated = appointmentId
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isCreatingAppointment = false,
                                errorMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Go to the next available date
     */
    @OptIn(ExperimentalTime::class)
    private fun goToNextAvailableDate() {
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
     * Get generated date options for the date selector
     */
    @OptIn(ExperimentalTime::class)
    private fun getDateOptions(selectedDate: Long = _uiState.value.selectedDate): List<DateOption> {
        val today = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
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
    @OptIn(ExperimentalTime::class)
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