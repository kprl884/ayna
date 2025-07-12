package org.style.customer.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.style.customer.ui.screens.home.HomeScreen

/**
 * Main Navigation Setup
 * Uses Voyager Navigator for cross-platform navigation
 */

@Composable
fun AynaAppNavigation() {
    Navigator(
        screen = HomeScreen(),
        content = { navigator ->
            SlideTransition(navigator)
        }
    )
} 