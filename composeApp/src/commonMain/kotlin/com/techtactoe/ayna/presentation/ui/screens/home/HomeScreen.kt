package com.techtactoe.ayna.presentation.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.techtactoe.ayna.presentation.navigation.Screen

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
    HomeScreenContent(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is HomeContract.UiEvent.OnSalonClick -> {
                    navController.navigate(Screen.Detail(event.salonId))
                }
                is HomeContract.UiEvent.OnSearchClick -> {
                    navController.navigate(Screen.Explore)
                }
                is HomeContract.UiEvent.OnProfileClick -> {
                    navController.navigate(Screen.Profile)
                }
                else -> {
                    onEvent(event)
                }
            }
        }
    )
}