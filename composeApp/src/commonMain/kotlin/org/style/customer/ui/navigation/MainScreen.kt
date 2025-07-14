package org.style.customer.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.style.customer.ui.screens.appointments.AppointmentsScreen
import org.style.customer.ui.screens.home.HomeScreen
import org.style.customer.ui.screens.profile.ProfileScreen
import org.style.customer.ui.screens.search.SearchScreen

/**
 * Main Screen with Bottom Navigation
 * This replaces the direct navigation to HomeScreen
 */
class MainScreen : Screen {
    @Composable
    override fun Content() {
        var selectedTabIndex by remember { mutableStateOf(0) }

        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedIndex = selectedTabIndex,
                    onItemClick = { index ->
                        selectedTabIndex = index
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTabIndex) {
                    0 -> HomeScreen().Content()
                    1 -> SearchScreen().Content()
                    2 -> AppointmentsScreen().Content()
                    3 -> ProfileScreen().Content()
                }
            }
        }
    }
}