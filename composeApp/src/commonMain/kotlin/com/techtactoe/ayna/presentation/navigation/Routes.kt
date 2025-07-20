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


}

/**
 * Extension function to get the icon for each screen
 */
val Screen.icon: ImageVector?
    get() = when (this) {
        is Screen.Home -> Icons.Default.Home
        is Screen.Search -> Icons.Default.Search
        is Screen.Appointments -> Icons.Default.DateRange
        is Screen.Profile -> Icons.Default.AccountCircle
        is Screen.Detail -> null
        is Screen.SalonDetailScreen -> null
    } 