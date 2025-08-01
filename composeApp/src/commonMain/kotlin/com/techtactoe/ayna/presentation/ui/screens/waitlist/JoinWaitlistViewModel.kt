package com.techtactoe.ayna.presentation.ui.screens.waitlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.WaitlistRequest
import com.techtactoe.ayna.domain.usecase.JoinWaitlistUseCase
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel for the Join Waitlist screen
 */
class JoinWaitlistViewModel(
    private val joinWaitlistUseCase: JoinWaitlistUseCase
) : ViewModel() {

    /**
     * Data class representing the UI state for the Join Waitlist screen
     */
    data class JoinWaitlistUiState(
        val isLoading: Boolean = false,
        val selectedDate: Long = System.currentTimeMillis(),
        val selectedTimeRange: String = "Any time",
        val error: String? = null,
        val isSuccess: Boolean = false,
        val hasAvailableSlots: Boolean = false,
        val availableSlotsMessage: String = ""
    )

    private val _uiState = MutableStateFlow(JoinWaitlistUiState())
    val uiState: StateFlow<JoinWaitlistUiState> = _uiState.asStateFlow()

    // Parameters from navigation
    private var salonId: String = ""
    private var serviceId: String = ""
    private val currentUserId = "user123" // In real app, from auth

    fun initialize(salonId: String, serviceId: String) {
        this.salonId = salonId
        this.serviceId = serviceId
    }

    /**
     * Update selected date
     */
    fun updateSelectedDate(date: Long) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        checkAvailableSlots()
    }

    /**
     * Update selected time range
     */
    fun updateSelectedTimeRange(timeRange: String) {
        _uiState.value = _uiState.value.copy(selectedTimeRange = timeRange)
    }

    /**
     * Join the waitlist
     */
    fun joinWaitlist() {
        viewModelScope.launch {
            val request = WaitlistRequest(
                userId = currentUserId,
                salonId = salonId,
                serviceId = serviceId,
                preferredDate = _uiState.value.selectedDate,
                preferredTimeRange = _uiState.value.selectedTimeRange
            )

            joinWaitlistUseCase(request).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
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
     * Check if there are available slots for the selected date
     */
    private fun checkAvailableSlots() {
        // Simulate checking for available slots
        // In this example, we'll say there are available slots for Saturday
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = _uiState.value.selectedDate
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        
        if (dayOfWeek == Calendar.SATURDAY) {
            _uiState.value = _uiState.value.copy(
                hasAvailableSlots = true,
                availableSlotsMessage = "There are time slots available to book on the selected dates, adjust your selection or book now."
            )
        } else {
            _uiState.value = _uiState.value.copy(
                hasAvailableSlots = false,
                availableSlotsMessage = ""
            )
        }
    }

    /**
     * Get formatted date string
     */
    fun getFormattedDate(): String {
        val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
        return dateFormat.format(Date(_uiState.value.selectedDate))
    }

    /**
     * Get available time range options
     */
    fun getTimeRangeOptions(): List<String> {
        return listOf("Any time", "Morning", "Afternoon", "Evening")
    }

    /**
     * Clear error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
