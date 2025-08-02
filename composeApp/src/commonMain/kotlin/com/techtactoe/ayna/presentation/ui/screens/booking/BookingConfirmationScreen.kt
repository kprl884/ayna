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
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 32.dp)
        )
        
        Text(
            text = "Booking Confirmed!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = "Your appointment has been successfully booked. You'll receive a confirmation email shortly.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Text(
            text = "Appointment ID: $appointmentId",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = onGoToAppointmentsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7B61FF)
            )
        ) {
            Text("View My Appointments")
        }
        
        OutlinedButton(
            onClick = onDoneClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Done")
        }
    }
}

@Preview
@Composable
private fun BookingConfirmationScreenPreview() {
    AynaAppTheme {
        BookingConfirmationScreen(
            appointmentId = "APT123456",
            onGoToAppointmentsClick = {},
            onDoneClick = {}
        )
    }
}
