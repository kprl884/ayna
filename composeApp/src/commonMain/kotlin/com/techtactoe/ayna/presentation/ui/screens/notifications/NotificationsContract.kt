package com.techtactoe.ayna.presentation.ui.screens.notifications

import com.techtactoe.ayna.domain.model.NotificationItem
import com.techtactoe.ayna.presentation.navigation.Screen

/**
 * Contract defining the UI state and events for the Notifications screen
 * Following the standardized MVVM pattern
 */
interface NotificationsContract {

    /**
     * Single source of truth for all UI state in the Notifications screen
     */
    data class UiState(
        // Data
        val notifications: List<NotificationItem> = emptyList(),

        // Loading states
        val isLoading: Boolean = false,

        // Settings
        val doNotDisturbEnabled: Boolean = false
    )

    /**
     * All possible user interactions with the Notifications screen
     */
    sealed interface UiEvent {
        // Data events
        data object OnInitialize : UiEvent
        data object OnMarkAllAsRead : UiEvent

        // Notification interaction events
        data class OnNotificationClick(val notification: NotificationItem) : UiEvent

        // Settings events
        data class OnToggleDoNotDisturb(val enabled: Boolean) : UiEvent
    }

    /**
     * Navigation events - handled separately
     */
    sealed interface NavigationEvent {
        data object NavigateBack : NavigationEvent
        data class NavigateToScreen(val screen: Screen) : NavigationEvent
    }
}