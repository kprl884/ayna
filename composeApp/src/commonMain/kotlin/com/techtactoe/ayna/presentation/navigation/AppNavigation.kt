package com.techtactoe.ayna.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.techtactoe.ayna.di.DataModule
import com.techtactoe.ayna.presentation.ui.components.AppBottomNavigation
import com.techtactoe.ayna.presentation.ui.screens.appointments.AppointmentsScreen
import com.techtactoe.ayna.presentation.ui.screens.booking.BookingConfirmationScreen
import com.techtactoe.ayna.presentation.ui.screens.explore.ExploreScreen
import com.techtactoe.ayna.presentation.ui.screens.home.HomeScreen
import com.techtactoe.ayna.presentation.ui.screens.profile.ProfileScreen
import com.techtactoe.ayna.presentation.ui.screens.salon.SalonDetailScreen
import com.techtactoe.ayna.presentation.ui.screens.salon.model.SalonDetailEffect
import com.techtactoe.ayna.presentation.ui.screens.search.SearchScreen
import com.techtactoe.ayna.presentation.ui.screens.selecttime.SelectTimeScreen
import com.techtactoe.ayna.presentation.ui.screens.waitlist.JoinWaitlistScreen

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

    val bottomBarScreens =
        setOf(Screen.Home, Screen.Search, Screen.Explore, Screen.Appointments, Screen.Profile)

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
                val viewModel = remember { DataModule.createHomeViewModel() }
                val uiState by viewModel.uiState.collectAsState()

                HomeScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    navController = navController
                )
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
                val viewModel = remember { DataModule.createAppointmentsViewModel() }
                val uiState by viewModel.uiState.collectAsState()

                AppointmentsScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    navController = navController
                )
            }
            composable<Screen.Profile> { ProfileScreen() }

            composable<Screen.Detail> { backStackEntry ->
                val screen: Screen.Detail = backStackEntry.toRoute()
                val salonId = screen.salonId

                // ViewModel is properly remembered to survive recompositions
                val viewModel = remember(salonId) { DataModule.createSalonDetailViewModel(salonId) }
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.effect.collect { effect ->
                        when (effect) {
                            is SalonDetailEffect.NavigateUp -> {
                                navController.popBackStack()
                            }

                            is SalonDetailEffect.NavigateToSelectTime -> {
                                navController.navigate(
                                    Screen.SelectTime(
                                        effect.salonId,
                                        effect.serviceId
                                    )
                                )
                            }

                            is SalonDetailEffect.Share -> {
                                // TODO: Implement platform-specific sharing logic here
                                println("Sharing: ${effect.text}")
                            }
                        }
                    }
                }

                SalonDetailScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent
                )
            }

            composable<Screen.SelectTime> { backStackEntry ->
                val screen: Screen.SelectTime = backStackEntry.toRoute()
                val viewModel = remember { DataModule.createSelectTimeViewModel() }

                SelectTimeScreen(
                    viewModel = viewModel,
                    salonId = screen.salonId,
                    serviceId = screen.serviceId,
                    onBackClick = { navController.popBackStack() },
                    onCloseClick = { navController.popBackStack() },
                    onTimeSelected = { timeSlot ->
                        // Create appointment and navigate to confirmation
                        viewModel.createAppointment("Sample Salon", "Sample Service")
                    },
                    onJoinWaitlistClick = {
                        navController.navigate(
                            Screen.JoinWaitlist(
                                screen.salonId,
                                screen.serviceId
                            )
                        )
                    }
                )

                // Listen for appointment creation success
                val uiState by viewModel.uiState.collectAsState()
                LaunchedEffect(uiState.appointmentCreated) {
                    uiState.appointmentCreated?.let { appointmentId ->
                        navController.navigate(Screen.BookingConfirmation(appointmentId)) {
                            popUpTo<Screen.SelectTime> { inclusive = true }
                        }
                    }
                }
            }

            composable<Screen.JoinWaitlist> { backStackEntry ->
                val screen: Screen.JoinWaitlist = backStackEntry.toRoute()
                val viewModel = remember { DataModule.createJoinWaitlistViewModel() }
                val uiState by viewModel.uiState.collectAsState()
                JoinWaitlistScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    salonId = screen.salonId,
                    serviceId = screen.serviceId,
                    onNavigateBack = { },
                    onNavigateClose = { },
                    onNavigateToContinue = { },
                    onNavigateToBooking = { },
                    onNavigateToSelectTime = { }
                )
            }

            composable<Screen.BookingConfirmation> { backStackEntry ->
                val screen: Screen.BookingConfirmation = backStackEntry.toRoute()
                val viewModel = remember { DataModule.createBookingConfirmationViewModel() }
                val uiState by viewModel.uiState.collectAsState()

                BookingConfirmationScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    appointmentId = screen.appointmentId,
                    navController = navController
                )
            }
        }
    }
}
