package com.techtactoe.ayna.presentation.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

/**
 * Screen displaying the home content with recommended salons
 * Following the golden standard MVVM pattern
 */
@Composable
fun HomeScreen(
    uiState: HomeContract.UiState,
    onEvent: (HomeContract.UiEvent) -> Unit,
    navController: NavController
) {
    // Handle navigation effects
    LaunchedEffect(uiState.navigateToSalonDetail) {
        uiState.navigateToSalonDetail?.let { salonId ->
            navController.navigate("salon_detail/$salonId")
            onEvent(HomeContract.UiEvent.OnNavigationHandled(HomeContract.NavigationReset.SALON_DETAIL))
        }
    }

    LaunchedEffect(uiState.navigateToSearch) {
        if (uiState.navigateToSearch) {
            navController.navigate("explore")
            onEvent(HomeContract.UiEvent.OnNavigationHandled(HomeContract.NavigationReset.SEARCH))
        }
    }

    LaunchedEffect(uiState.navigateToProfile) {
        if (uiState.navigateToProfile) {
            navController.navigate("profile")
            onEvent(HomeContract.UiEvent.OnNavigationHandled(HomeContract.NavigationReset.PROFILE))
        }
    }

    HomeScreenContent(
        uiState = uiState,
        onEvent = onEvent
    )
}
