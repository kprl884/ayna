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
    val route: String

    @Serializable
    data object Home : Screen {
        override val route: String = "home"
    }

    @Serializable
    data object Search : Screen {
        override val route: String = "search"
    }

    @Serializable
    data object Explore : Screen {
        override val route: String = "explore"
    }

    @Serializable
    data object ExploreMap : Screen {
        override val route: String = "explore_map"
    }

    @Serializable
    data object AdvancedSearch : Screen {
        override val route: String = "advanced_search"
    }
    @Serializable
    data object Appointments : Screen {
        override val route: String = "appointments"
    }

    @Serializable
    data object Profile : Screen {
        override val route: String = "profile"
    }

    @Serializable
    data class Detail(val salonId: String) : Screen {
        override val route: String = "detail"
    }

    @Serializable
    data class SalonDetailScreen(val salonId: String) : Screen {
        override val route: String = "salon_detail"
    }

    @Serializable
    data class SelectTime(val salonId: String, val serviceId: String) : Screen {
        override val route: String = "select_time"
    }

    @Serializable
    data class JoinWaitlist(val salonId: String, val serviceId: String) : Screen {
        override val route: String = "join_waitlist"
    }

    @Serializable
    data class BookingConfirmation(val appointmentId: String) : Screen {
        override val route: String = "booking_confirmation"
    }
}

/**
 * Extension function to get the icon for each screen
 */
val Screen.icon: ImageVector?
    get() = when (this) {
        is Screen.Home -> Icons.Default.Home
        is Screen.Search -> Icons.Default.Search
        is Screen.Explore -> Icons.Default.Search
        is Screen.Appointments -> Icons.Default.DateRange
        is Screen.Profile -> Icons.Default.AccountCircle
        is Screen.Detail -> null
        is Screen.SalonDetailScreen -> null
        is Screen.ExploreMap -> null
        is Screen.AdvancedSearch -> null
        is Screen.SelectTime -> null
        is Screen.JoinWaitlist -> null
        is Screen.BookingConfirmation -> null
    }
