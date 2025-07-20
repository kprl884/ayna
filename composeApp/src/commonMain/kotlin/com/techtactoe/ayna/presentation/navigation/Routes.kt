package com.techtactoe.ayna.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{salonId}") {
        fun createRoute(salonId: String) = "detail/$salonId"
    }
} 