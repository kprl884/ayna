package com.techtactoe.ayna.presentation.ui.screens.appointments

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.common.designsystem.theme.AynaAppTheme
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.domain.model.Review
import com.techtactoe.ayna.domain.model.TeamMember
import com.techtactoe.ayna.presentation.ui.components.ReviewsSection
import com.techtactoe.ayna.presentation.ui.components.ReviewsSectionViewState
import com.techtactoe.ayna.presentation.ui.components.TeamSection
import com.techtactoe.ayna.presentation.ui.components.TeamSectionViewState
import com.techtactoe.ayna.presentation.ui.screens.salon.components.ImageCarousel
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Appointment Detail screen: mirrors Salon detail structure where relevant.
 * Sections: hero images, opening hours, reviews, experts, services, appointment info,
 * payment status, postpone/cancel buttons, expandable map view with a marker.
 */
@Composable
fun AppointmentDetailScreen(
    appointmentId: String,
    onBackClick: () -> Unit,
) {
    val showCancelDialog = remember { mutableStateOf(false) }

    // In absence of a repository for details, mock a minimal appointment entity for display
    val mockAppointment = remember(appointmentId) {
        Appointment(
            id = appointmentId,
            salonId = "salon-1",
            salonName = "Ayna Beauty Studio",
            serviceName = "Haircut & Styling",
            employeeId = "emp-42",
            employeeName = "Elif Kaya",
            appointmentDateTime = kotlinx.datetime.Clock.System.now().toEpochMilliseconds() + 86_400_000L,
            status = AppointmentStatus.UPCOMING,
            price = 650.0,
            durationInMinutes = 60,
            notes = "Please prepare for curly hair."
        )
    }

    val isPaid = remember { mutableStateOf(mockAppointment.price > 0) }
    val mapExpanded = remember { mutableStateOf(false) }

    AynaAppTheme {
        Surface {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Image carousel (top)
                item {
                    val images = listOf(
                        "https://images.unsplash.com/photo-1517836357463-d25dfeac3438",
                        "https://images.unsplash.com/photo-1556228720-195a672e8a03",
                        "https://images.unsplash.com/photo-1514996937319-344454492b37"
                    )
                    ImageCarousel(
                        images = images,
                        onBackClick = { onBackClick() },
                        onShareClick = { /* TODO share */ },
                        onFavoriteClick = { /* TODO favorite */ }
                    )
                }

                // Appointment basic info card
                item {
                    AppointmentInfoSection(mockAppointment, isPaid)
                }

                // Services (booked service overview)
                item {
                    ServicesOverviewSection(serviceName = mockAppointment.serviceName, price = mockAppointment.price, durationMin = mockAppointment.durationInMinutes)
                }

                // Experts section (team)
                item {
                    TeamSection(
                        TeamSectionViewState(
                            teamMembers = listOf(
                                TeamMember(id = "1", name = "Elif Kaya", role = "Senior Stylist", imageUrl = null, rating = 5.0),
                                TeamMember(id = "2", name = "Mert", role = "Color Specialist", imageUrl = null, rating = 4.9)
                            )
                        )
                    )
                }

                // Opening hours section (simple)
                item {
                    OpeningHoursSection(
                        hours = listOf(
                            "Mon" to "09:00 - 19:00",
                            "Tue" to "09:00 - 19:00",
                            "Wed" to "09:00 - 19:00",
                            "Thu" to "09:00 - 20:00",
                            "Fri" to "09:00 - 20:00",
                            "Sat" to "10:00 - 18:00",
                            "Sun" to "Closed"
                        )
                    )
                }

                // Reviews section
                item {
                    ReviewsSection(
                        ReviewsSectionViewState(
                            reviews = listOf(
                                Review(id = "1", userName = "Ayşe", userInitials = "A", date = "Aug 7, 2025", rating = 5, comment = "Great experience!"),
                                Review(id = "2", userName = "Can", userInitials = "C", date = "Aug 1, 2025", rating = 4, comment = "Very professional and friendly.")
                            ),
                            overallRating = 4.9,
                            reviewCount = 124
                        )
                    )
                }

                // Expandable map view with marker
                item {
                    ExpandableMapSection(expandedState = mapExpanded)
                }

                // Action buttons
                item {
                    ActionButtons(
                        onPostpone = { /* TODO: Navigate to reschedule flow if/when available */ },
                        onCancel = { showCancelDialog.value = true }
                    )
                }

                item { Spacer(modifier = Modifier.height(Spacing.large)) }
            }

            if (showCancelDialog.value) {
                AlertDialog(
                    onDismissRequest = { showCancelDialog.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            // TODO: Handle cancel action via ViewModel/use case
                            showCancelDialog.value = false
                            onBackClick()
                        }) { Text("Confirm") }
                    },
                    dismissButton = { TextButton(onClick = { showCancelDialog.value = false }) { Text("Dismiss") } },
                    title = { Text("Cancel appointment?") },
                    text = { Text("Are you sure you want to cancel this appointment?") }
                )
            }
        }
    }
}

@Composable
private fun AppointmentInfoSection(appointment: Appointment, isPaid: MutableState<Boolean>) {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = Spacing.medium, vertical = Spacing.medium)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Spacing.medium), verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            Text(appointment.salonName, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(appointment.serviceName, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            val dt = Instant.fromEpochMilliseconds(appointment.appointmentDateTime).toLocalDateTime(TimeZone.currentSystemDefault())
            val month = when (dt.month) {
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
                else -> dt.month.name.take(3)
            }
            val hour = if (dt.hour == 0) 12 else if (dt.hour > 12) dt.hour - 12 else dt.hour
            val amPm = if (dt.hour < 12) "AM" else "PM"
            val formattedDate = "$month ${dt.dayOfMonth}, ${dt.year} at $hour:${dt.minute.toString().padStart(2, '0')} $amPm"

            Text("$formattedDate • ${appointment.durationInMinutes} min", style = MaterialTheme.typography.bodyMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.small), verticalAlignment = Alignment.CenterVertically) {
                val statusColor = when (appointment.status) {
                    AppointmentStatus.UPCOMING -> MaterialTheme.colorScheme.primary
                    AppointmentStatus.COMPLETED -> MaterialTheme.colorScheme.onSurfaceVariant
                    AppointmentStatus.CANCELLED -> MaterialTheme.colorScheme.error
                }
                StatusChip(text = appointment.status.name.lowercase().replaceFirstChar { it.uppercase() }, color = statusColor)
                StatusChip(text = if (isPaid.value) "Paid" else "Unpaid", color = if (isPaid.value) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error)
                StatusChip(text = "₺${appointment.price.toInt()}", color = MaterialTheme.colorScheme.secondary)
            }

            if (appointment.notes.isNotBlank()) {
                Divider(modifier = Modifier.padding(vertical = Spacing.small))
                Text("Notes", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                Text(appointment.notes, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun StatusChip(text: String, color: Color) {
    Surface(color = color.copy(alpha = 0.1f), shape = MaterialTheme.shapes.extraSmall) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = Spacing.small, vertical = Spacing.extraSmall)
        )
    }
}

@Composable
private fun ServicesOverviewSection(serviceName: String, price: Double, durationMin: Int) {
    Column(modifier = Modifier.padding(horizontal = Spacing.medium)) {
        Text("Services", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = Spacing.small))
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(Spacing.medium), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(serviceName, style = MaterialTheme.typography.titleMedium)
                    Text("$durationMin min", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("₺${price.toInt()}", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun OpeningHoursSection(hours: List<Pair<String, String>>) {
    Column(modifier = Modifier.padding(horizontal = Spacing.medium, vertical = Spacing.medium)) {
        Text("Opening hours", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(Spacing.small))
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(Spacing.medium), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                hours.forEach { (day, range) ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(day, style = MaterialTheme.typography.bodyMedium)
                        Text(range, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableMapSection(expandedState: MutableState<Boolean>) {
    Column(modifier = Modifier.padding(horizontal = Spacing.medium, vertical = Spacing.medium)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Location", style = MaterialTheme.typography.titleLarge)
            Text(
                if (expandedState.value) "Collapse" else "Expand",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { expandedState.value = !expandedState.value }
            )
        }
        Spacer(Modifier.height(Spacing.small))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (expandedState.value) 240.dp else 160.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .animateContentSize(),
            contentAlignment = Alignment.Center
        ) {
            // Simple marker placeholder
            Surface(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small) {
                Text("Marker", color = Color.White, modifier = Modifier.padding(horizontal = Spacing.small, vertical = Spacing.extraSmall))
            }
        }
    }
}

@Composable
private fun ActionButtons(onPostpone: () -> Unit, onCancel: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = Spacing.medium, vertical = Spacing.medium)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        Button(
            onClick = onPostpone,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) { Text("Postpone") }

        Button(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) { Text("Cancel appointment") }
    }
}

@Preview
@Composable
private fun AppointmentDetailScreenPreview() {
    AppointmentDetailScreen(appointmentId = "apt-123", onBackClick = {})
}
