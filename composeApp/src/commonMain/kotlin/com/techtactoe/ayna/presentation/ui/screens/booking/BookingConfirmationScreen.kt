package com.techtactoe.ayna.presentation.ui.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.techtactoe.ayna.presentation.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.theme.Spacing
import com.techtactoe.ayna.presentation.theme.brandPurple
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
    // Initialize with appointment ID
    LaunchedEffect(appointmentId) {
        onEvent(BookingConfirmationContract.UiEvent.OnInitialize(appointmentId))
    }

    // Handle navigation effects
    LaunchedEffect(uiState.navigateToAppointments) {
        if (uiState.navigateToAppointments) {
            navController.navigate("appointments") {
                popUpTo("home") { inclusive = false }
            }
            onEvent(BookingConfirmationContract.UiEvent.OnNavigationHandled(BookingConfirmationContract.NavigationReset.APPOINTMENTS))
        }
    }

    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
            onEvent(BookingConfirmationContract.UiEvent.OnNavigationHandled(BookingConfirmationContract.NavigationReset.HOME))
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

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.brandPurple)
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onClearError: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.md)
        )

        Button(
            onClick = onRetry,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Try again",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun ConfirmationContent(
    uiState: BookingConfirmationContract.UiState,
    onEvent: (BookingConfirmationContract.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(Spacing.xxxl + Spacing.md)
                .padding(bottom = Spacing.xl)
        )

        Text(
            text = "Booking Confirmed!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.md)
        )

        Text(
            text = "Your appointment has been successfully booked. You'll receive a confirmation email shortly.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.xl)
        )

        // Appointment details
        if (uiState.salonName.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.lg),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.md)
                ) {
                    Text(
                        text = uiState.salonName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = uiState.serviceName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Spacing.xs)
                    )

                    Text(
                        text = uiState.appointmentDateTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Spacing.xs)
                    )

                    if (uiState.price.isNotEmpty()) {
                        Text(
                            text = uiState.price,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = Spacing.sm)
                        )
                    }
                }
            }
        }

        Text(
            text = "Appointment ID: ${uiState.appointmentId}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.xl)
        )

        Button(
            onClick = { onEvent(BookingConfirmationContract.UiEvent.OnGoToAppointmentsClick) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.md),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.brandPurple
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "View My Appointments",
                style = MaterialTheme.typography.labelLarge
            )
        }

        OutlinedButton(
            onClick = { onEvent(BookingConfirmationContract.UiEvent.OnDoneClick) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Done",
                style = MaterialTheme.typography.labelLarge
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
