package com.techtactoe.ayna.presentation.ui.screens.booking.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.presentation.ui.screens.booking.BookingConfirmationContract

@Composable
fun ConfirmationContent(
    uiState: BookingConfirmationContract.UiState,
    onEvent: (BookingConfirmationContract.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xlarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(Spacing.xxxlarge + Spacing.medium)
                .padding(bottom = Spacing.xlarge)
        )

        Text(
            text = "Booking Confirmed!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.medium)
        )

        Text(
            text = "Your appointment has been successfully booked. You'll receive a confirmation email shortly.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.xlarge)
        )

        // Appointment details
        if (uiState.salonName.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.large),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.medium)
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
                        modifier = Modifier.padding(top = Spacing.extraSmall)
                    )

                    Text(
                        text = uiState.appointmentDateTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Spacing.extraSmall)
                    )

                    if (uiState.price.isNotEmpty()) {
                        Text(
                            text = uiState.price,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = Spacing.small)
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
            modifier = Modifier.padding(bottom = Spacing.xlarge)
        )

        Button(
            onClick = { onEvent(BookingConfirmationContract.UiEvent.OnGoToAppointmentsClick) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.medium),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
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