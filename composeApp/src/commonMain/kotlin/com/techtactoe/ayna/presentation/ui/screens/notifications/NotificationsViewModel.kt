package com.techtactoe.ayna.presentation.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.NotificationItem
import com.techtactoe.ayna.domain.model.NotificationType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Notifications screen following the golden standard MVVM pattern
 */
class NotificationsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsContract.UiState())
    val uiState: StateFlow<NotificationsContract.UiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    /**
     * Handle all user events from the UI
     */
    fun onEvent(event: NotificationsContract.UiEvent) {
        when (event) {
            is NotificationsContract.UiEvent.OnInitialize -> {
                loadNotifications()
                // Auto-mark as read after 2 seconds
                markUnreadAsReadAfterDelay()
            }
            is NotificationsContract.UiEvent.OnMarkAllAsRead -> {
                markAllAsRead()
            }
            is NotificationsContract.UiEvent.OnNotificationClick -> {
                handleNotificationClick(event.notification)
            }
            is NotificationsContract.UiEvent.OnToggleDoNotDisturb -> {
                _uiState.update { it.copy(doNotDisturbEnabled = event.enabled) }
                // TODO: Save to preferences
            }
            is NotificationsContract.UiEvent.OnBackClick -> {
                _uiState.update { it.copy(navigateBack = true) }
            }
            is NotificationsContract.UiEvent.OnNavigationHandled -> {
                when (event.resetNavigation) {
                    NotificationsContract.NavigationReset.BACK -> {
                        _uiState.update { it.copy(navigateBack = false) }
                    }
                    NotificationsContract.NavigationReset.ROUTE -> {
                        _uiState.update { it.copy(navigateToRoute = null) }
                    }
                }
            }
        }
    }

    private fun loadNotifications() {
        _uiState.update { it.copy(isLoading = true) }
        
        // Sample notifications data
        val sampleNotifications = listOf(
            NotificationItem(
                id = "1",
                message = "You have an appointment at The Galleria Hair Salon at 8:00am today",
                timestamp = "Just now",
                isRead = false,
                type = NotificationType.APPOINTMENT_REMINDER
            ),
            NotificationItem(
                id = "2",
                message = "Your password is successfully changed",
                timestamp = "2 hours ago",
                isRead = true,
                type = NotificationType.PASSWORD_CHANGED
            ),
            NotificationItem(
                id = "3",
                message = "Completed your profile to be better health consults.",
                timestamp = "3 days ago",
                isRead = false,
                type = NotificationType.PROFILE_COMPLETION,
                actionText = "Complete Profile",
                actionRoute = "/profile"
            ),
            NotificationItem(
                id = "4",
                message = "Your appointment has been cancelled by Dr. Smith",
                timestamp = "1 day ago",
                isRead = true,
                type = NotificationType.APPOINTMENT_CANCELLED
            ),
            NotificationItem(
                id = "5",
                message = "Appointment reminder: Consultation tomorrow at 2:00 PM",
                timestamp = "5 hours ago",
                isRead = false,
                type = NotificationType.APPOINTMENT_REMINDER
            )
        )
        
        _uiState.update {
            it.copy(
                notifications = sampleNotifications,
                isLoading = false
            )
        }
    }

    private fun markAllAsRead() {
        val updatedNotifications = _uiState.value.notifications.map { notification ->
            notification.copy(isRead = true)
        }
        _uiState.update { it.copy(notifications = updatedNotifications) }
    }

    private fun markUnreadAsReadAfterDelay() {
        viewModelScope.launch {
            delay(2000) // Wait 2 seconds
            markAllAsRead()
        }
    }

    private fun handleNotificationClick(notification: NotificationItem) {
        // Mark notification as read
        val updatedNotifications = _uiState.value.notifications.map { 
            if (it.id == notification.id) it.copy(isRead = true) else it
        }
        _uiState.update { it.copy(notifications = updatedNotifications) }
        
        // Navigate based on notification type
        val route = when (notification.type) {
            NotificationType.APPOINTMENT_REMINDER,
            NotificationType.APPOINTMENT_CANCELLED -> "/appointment/${notification.id}"
            NotificationType.PROFILE_COMPLETION -> notification.actionRoute ?: "/profile"
            NotificationType.PASSWORD_CHANGED -> "/settings"
        }
        
        _uiState.update { it.copy(navigateToRoute = route) }
    }
}
