package org.style.customer.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Calendar
import compose.icons.fontawesomeicons.regular.User
import compose.icons.fontawesomeicons.solid.Calendar
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.User

/**
 * Bottom Navigation Items
 * Represents each tab in the bottom navigation
 */
data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

object BottomNavItems {
    val items = listOf(
        BottomNavItem(
            route = "home",
            title = "Ana Sayfa",
            selectedIcon = FontAwesomeIcons.Solid.Home,
            unselectedIcon = FontAwesomeIcons.Solid.Home,
        ),
        BottomNavItem(
            route = "search",
            title = "Arama",
            selectedIcon = FontAwesomeIcons.Solid.Search,
            unselectedIcon = FontAwesomeIcons.Solid.Search
        ),
        BottomNavItem(
            route = "appointments",
            title = "Randevular",
            selectedIcon = FontAwesomeIcons.Solid.Calendar,
            unselectedIcon = FontAwesomeIcons.Regular.Calendar
        ),
        BottomNavItem(
            route = "profile",
            title = "Profil",
            selectedIcon = FontAwesomeIcons.Solid.User,
            unselectedIcon = FontAwesomeIcons.Regular.User
        )
    )
} 