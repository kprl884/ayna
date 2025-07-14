package org.style.customer.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.style.customer.ui.screens.splash.SplashScreen

/**
 * Main Navigation Setup
 * Uses Voyager Navigator for cross-platform navigation
 */
@Composable
fun AynaAppNavigation() {
    Navigator(
        screen = SplashScreen(),
        content = { navigator ->
            SlideTransition(navigator)
        }
    )
} 