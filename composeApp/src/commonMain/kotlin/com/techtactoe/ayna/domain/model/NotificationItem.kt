package com.techtactoe.ayna.domain.model

/**
 * Data model for notification items
 */
data class NotificationItem(
    val id: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: NotificationType,
    val actionText: String? = null,
    val actionRoute: String? = null
)

/**
 * Types of notifications in the app
 */
enum class NotificationType {
    APPOINTMENT_REMINDER,
    APPOINTMENT_CANCELLED,
    PROFILE_COMPLETION,
    PASSWORD_CHANGED
}

/**
 * UI state for notification-related features
 */
data class NotificationUiState(
    val hasUnreadNotifications: Boolean = false,
    val unreadCount: Int = 0
)
