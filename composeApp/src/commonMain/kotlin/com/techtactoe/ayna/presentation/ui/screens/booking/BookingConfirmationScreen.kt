package com.techtactoe.ayna.presentation.ui.screens.booking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.techtactoe.ayna.common.designsystem.ErrorContent
import com.techtactoe.ayna.common.designsystem.LoadingContent
import com.techtactoe.ayna.common.designsystem.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.ui.screens.booking.components.ConfirmationContent
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen displaying booking confirmation details
 * Following the golden standard MVVM pattern
 */
@Composable
fun BookingConfirmationScreen(
    uiState: BookingConfirmationContract.UiState,
    onEvent: (BookingConfirmationContract.UiEvent) -> Unit,
    appointmentId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(appointmentId) {
        onEvent(BookingConfirmationContract.UiEvent.OnInitialize(appointmentId))
    }

    // Handle navigation effects
    LaunchedEffect(uiState.navigateToAppointments) {
        if (uiState.navigateToAppointments) {
            navController.navigate("appointments") {
                popUpTo("home") { inclusive = false }
            }
            onEvent(
                BookingConfirmationContract.UiEvent.OnNavigationHandled(
                    BookingConfirmationContract.NavigationReset.APPOINTMENTS
                )
            )
        }
    }

    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
            onEvent(
                BookingConfirmationContract.UiEvent.OnNavigationHandled(
                    BookingConfirmationContract.NavigationReset.HOME
                )
            )
        }
    }

    when {
        uiState.isLoading -> {
            LoadingContent()
        }

        uiState.errorMessage != null -> {
            ErrorContent(
                message = uiState.errorMessage,
                onRetry = { onEvent(BookingConfirmationContract.UiEvent.OnInitialize(appointmentId)) },
                onClearError = { onEvent(BookingConfirmationContract.UiEvent.OnClearError) }
            )
        }

        uiState.isConfirmed -> {
            ConfirmationContent(
                uiState = uiState,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}

@Preview
@Composable
private fun BookingConfirmationScreenPreview() {
    AynaAppTheme {
        ConfirmationContent(
            uiState = BookingConfirmationContract.UiState(
                appointmentId = "APT123456",
                salonName = "Beauty Salon",
                serviceName = "Haircut & Styling",
                appointmentDateTime = "Tomorrow at 2:00 PM",
                price = "₺150",
                isConfirmed = true
            ),
            onEvent = {}
        )
    }
}
