package com.techtactoe.ayna.presentation.ui.screens.appointments.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.presentation.theme.Elevation
import com.techtactoe.ayna.presentation.theme.Spacing
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appointmentDateTime = Instant.fromEpochMilliseconds(appointment.appointmentDateTime)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val month = when (appointmentDateTime.month) {
        Month.JANUARY -> "Jan"
        Month.FEBRUARY -> "Feb"
        Month.MARCH -> "Mar"
        Month.APRIL -> "Apr"
        Month.MAY -> "May"
        Month.JUNE -> "Jun"
        Month.JULY -> "Jul"
        Month.AUGUST -> "Aug"
        Month.SEPTEMBER -> "Sep"
        Month.OCTOBER -> "Oct"
        Month.NOVEMBER -> "Nov"
        Month.DECEMBER -> "Dec"
        else -> {}
    }

    val hour =
        if (appointmentDateTime.hour == 0) 12 else if (appointmentDateTime.hour > 12) appointmentDateTime.hour - 12 else appointmentDateTime.hour
    val amPm = if (appointmentDateTime.hour < 12) "AM" else "PM"
    val formattedDate =
        "$month ${appointmentDateTime.dayOfMonth}, ${appointmentDateTime.year} at $hour:${
            appointmentDateTime.minute.toString().padStart(2, '0')
        } $amPm"

    val statusColor = when (appointment.status) {
        AppointmentStatus.UPCOMING -> MaterialTheme.colorScheme.primary
        AppointmentStatus.COMPLETED -> MaterialTheme.colorScheme.onSurfaceVariant
        AppointmentStatus.CANCELLED -> MaterialTheme.colorScheme.error
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.sm),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appointment.salonName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = appointment.serviceName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Spacing.xs)
                    )

                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Spacing.xs)
                    )
                }

                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = appointment.status.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        modifier = Modifier.padding(
                            horizontal = Spacing.sm,
                            vertical = Spacing.xs
                        )
                    )
                }
            }

            if (appointment.employeeName.isNotBlank()) {
                Text(
                    text = "with ${appointment.employeeName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = Spacing.sm)
                )
            }

            if (appointment.price > 0) {
                Text(
                    text = "₺${appointment.price.toInt()}",
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