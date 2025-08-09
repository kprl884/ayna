package com.techtactoe.ayna.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Explore : Screen

    @Serializable
    data object ExploreMap : Screen

    @Serializable
    data object AdvancedSearch : Screen

    @Serializable
    data object Appointments : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object Notifications : Screen

    @Serializable
    data class Detail(val salonId: String) : Screen

    @Serializable
    data class SelectTime(val salonId: String, val serviceId: String) : Screen

    @Serializable
    data class JoinWaitlist(val salonId: String, val serviceId: String) : Screen

    @Serializable
    data class BookingConfirmation(val appointmentId: String) : Screen

    @Serializable
    data class AppointmentDetail(val appointmentId: String) : Screen
}

/**
 * Extension function to get the icon for each screen
 */
val Screen.icon: ImageVector?
    get() = when (this) {
        is Screen.Home -> Icons.Default.Home
        is Screen.Explore -> Icons.Default.Search
        is Screen.Appointments -> Icons.Default.DateRange
        is Screen.Profile -> Icons.Default.AccountCircle
        else -> null
    }
