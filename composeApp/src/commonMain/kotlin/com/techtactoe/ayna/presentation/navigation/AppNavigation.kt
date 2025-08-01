package com.techtactoe.ayna.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.techtactoe.ayna.data.MockSalonDetailRepository
import com.techtactoe.ayna.di.DataModule
import com.techtactoe.ayna.presentation.ui.components.AppBottomNavigation
import com.techtactoe.ayna.presentation.ui.screens.appointments.AppointmentsScreen
import com.techtactoe.ayna.presentation.ui.screens.home.HomeScreen
import com.techtactoe.ayna.presentation.ui.screens.profile.ProfileScreen
import com.techtactoe.ayna.presentation.ui.screens.salon.SalonDetailScreen
import com.techtactoe.ayna.presentation.ui.screens.search.SearchScreen
import com.techtactoe.ayna.presentation.ui.screens.explore.ExploreScreen
import com.techtactoe.ayna.presentation.ui.screens.selecttime.SelectTimeScreen
import com.techtactoe.ayna.presentation.ui.screens.waitlist.JoinWaitlistScreen
import com.techtactoe.ayna.presentation.ui.screens.booking.BookingConfirmationScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    val currentScreen: Screen? = when {
        currentRoute?.contains("com.techtactoe.ayna.presentation.navigation.Screen.Home") == true -> Screen.Home
        currentRoute?.contains("com.techtactoe.ayna.presentation.navigation.Screen.Search") == true -> Screen.Search
        currentRoute?.contains("com.techtactoe.ayna.presentation.navigation.Screen.Explore") == true -> Screen.Explore
        currentRoute?.contains("com.techtactoe.ayna.presentation.navigation.Screen.Appointments") == true -> Screen.Appointments
        currentRoute?.contains("com.techtactoe.ayna.presentation.navigation.Screen.Profile") == true -> Screen.Profile
        currentRoute?.contains("com.techtactoe.ayna.presentation.navigation.Screen.Detail") == true -> null // Don't show bottom bar
        else -> null
    }

    val bottomBarScreens = setOf(Screen.Home, Screen.Search, Screen.Explore, Screen.Appointments, Screen.Profile)

    Scaffold(
        bottomBar = {
            if (currentScreen in bottomBarScreens) {
                AppBottomNavigation(
                    currentScreen = currentScreen,
                    onItemSelected = { screen ->
                        navController.navigate(screen) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Screen.Home> {
                HomeScreen(onSalonClick = { salonId ->
                    navController.navigate(Screen.Detail(salonId))
                })
            }
            composable<Screen.Search> { SearchScreen() }
            composable<Screen.Explore> {
                ExploreScreen(
                    onNavigateToVenueDetail = { venueId ->
                        navController.navigate(Screen.Detail(venueId))
                    },
                    onNavigateToMap = {
                        navController.navigate(Screen.ExploreMap)
                    },
                    onNavigateToAdvancedSearch = {
                        navController.navigate(Screen.AdvancedSearch)
                    }
                )
            }
            composable<Screen.ExploreMap> {
                // TODO: Implement map screen
                SearchScreen() // Placeholder
            }
            composable<Screen.AdvancedSearch> {
                // TODO: Implement advanced search screen
                SearchScreen() // Placeholder
            }
            composable<Screen.Appointments> {
                AppointmentsScreen(
                    viewModel = DataModule.createAppointmentsViewModel()
                )
            }
            composable<Screen.Profile> { ProfileScreen() }

            composable<Screen.Detail> { backStackEntry ->
                val screen: Screen.Detail = backStackEntry.toRoute()
                val salonId = screen.salonId

                // Get salon detail data
                val salonDetail = MockSalonDetailRepository.getSalonDetail(salonId)

                SalonDetailScreen(
                    salonDetail = salonDetail,
                    onBackClick = { navController.popBackStack() },
                    onShareClick = { /* Handle share */ },
                    onFavoriteClick = { /* Handle favorite */ },
                    onBookNowClick = { /* Handle booking */ },
                    onServiceBookClick = { serviceId ->
                        navController.navigate(Screen.SelectTime(salonId, serviceId))
                    }
                )
            }

            composable<Screen.SelectTime> { backStackEntry ->
                val screen: Screen.SelectTime = backStackEntry.toRoute()

                SelectTimeScreen(
                    viewModel = DataModule.createSelectTimeViewModel(),
                    salonId = screen.salonId,
                    serviceId = screen.serviceId,
                    onBackClick = { navController.popBackStack() },
                    onCloseClick = { navController.popBackStack() },
                    onTimeSelected = { timeSlot ->
                        // TODO: Navigate to booking confirmation or handle selected time
                    },
                    onJoinWaitlistClick = {
                        navController.navigate(Screen.JoinWaitlist(screen.salonId, screen.serviceId))
                    }
                )
            }

            composable<Screen.JoinWaitlist> { backStackEntry ->
                val screen: Screen.JoinWaitlist = backStackEntry.toRoute()

                JoinWaitlistScreen(
                    viewModel = DataModule.createJoinWaitlistViewModel(),
                    salonId = screen.salonId,
                    serviceId = screen.serviceId,
                    onBackClick = { navController.popBackStack() },
                    onCloseClick = { navController.popBackStack() },
                    onContinueClick = {
                        // TODO: Navigate to booking confirmation or handle waitlist submission
                    }
                )
            }
        }
    }
}
