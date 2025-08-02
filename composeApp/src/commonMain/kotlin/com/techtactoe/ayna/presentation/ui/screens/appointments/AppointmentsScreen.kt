package com.techtactoe.ayna.presentation.ui.screens.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.presentation.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.theme.Spacing
import com.techtactoe.ayna.presentation.theme.CornerRadius
import com.techtactoe.ayna.presentation.theme.Elevation
import com.techtactoe.ayna.presentation.theme.brandPurple
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen displaying user appointments following the golden standard MVVM pattern
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    uiState: AppointmentsContract.UiState,
    onEvent: (AppointmentsContract.UiEvent) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Handle navigation effects
    LaunchedEffect(uiState.navigateToSearch) {
        if (uiState.navigateToSearch) {
            navController.navigate("explore")
            onEvent(AppointmentsContract.UiEvent.OnNavigationHandled(AppointmentsContract.NavigationReset.SEARCH))
        }
    }

    LaunchedEffect(uiState.navigateToAppointmentDetail) {
        uiState.navigateToAppointmentDetail?.let { appointmentId ->
            navController.navigate("appointment_detail/$appointmentId")
            onEvent(AppointmentsContract.UiEvent.OnNavigationHandled(AppointmentsContract.NavigationReset.APPOINTMENT_DETAIL))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Appointments",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingContent()
                }

                uiState.errorMessage != null -> {
                    ErrorContent(
                        message = uiState.errorMessage,
                        onRetry = { onEvent(AppointmentsContract.UiEvent.OnRefresh) },
                        onClearError = { onEvent(AppointmentsContract.UiEvent.OnClearError) }
                    )
                }

                uiState.isEmpty -> {
                    EmptyAppointmentsContent(
                        onSearchSalonsClick = { onEvent(AppointmentsContract.UiEvent.OnNavigateToSearch) }
                    )
                }

                else -> {
                    Column {
                        TabRow(
                            selectedTabIndex = uiState.selectedTab.ordinal,
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            AppointmentsContract.AppointmentTab.values().forEach { tab ->
                                Tab(
                                    selected = uiState.selectedTab == tab,
                                    onClick = { onEvent(AppointmentsContract.UiEvent.OnTabSelected(tab)) },
                                    text = {
                                        Text(
                                            text = when (tab) {
                                                AppointmentsContract.AppointmentTab.UPCOMING -> "Upcoming"
                                                AppointmentsContract.AppointmentTab.PAST -> "Past"
                                            },
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                )
                            }
                        }

                        AppointmentsContent(
                            selectedTab = uiState.selectedTab,
                            upcomingAppointments = uiState.upcomingAppointments,
                            pastAppointments = uiState.pastAppointments,
                            onAppointmentClick = { appointment ->
                                onEvent(AppointmentsContract.UiEvent.OnAppointmentClicked(appointment.id))
                            }
                        )
                    }
                }
            }
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
private fun EmptyAppointmentsContent(
    onSearchSalonsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Custom purple gradient calendar icon matching the Figma design
        PurpleCalendarIcon(
            modifier = Modifier.padding(bottom = Spacing.xl)
        )

        Text(
            text = "No appointments",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.md)
        )

        Text(
            text = "Your upcoming and past appointments\nwill appear here when you book",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.xxl)
        )

        OutlinedButton(
            onClick = onSearchSalonsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.xl),
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                "Search salons",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = Spacing.xs)
            )
        }
    }
}

@Composable
private fun PurpleCalendarIcon(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9C7BFF), // Lighter purple at top
                        Color(0xFF7B61FF)  // Brand purple at bottom
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Calendar icon structure
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top section with small rectangles (calendar binding)
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(Color.White.copy(alpha = 0.9f))
                    )
                }
            }

            // Main calendar body
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.95f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Calendar grid dots
                    repeat(3) { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            repeat(3) { col ->
                                Box(
                                    modifier = Modifier
                                        .size(3.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(
                                            if (row == 1 && col == 1) Color(0xFF7B61FF)
                                            else Color(0xFFD1C4E9)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppointmentsContent(
    selectedTab: AppointmentsContract.AppointmentTab,
    upcomingAppointments: List<Appointment>,
    pastAppointments: List<Appointment>,
    onAppointmentClick: (Appointment) -> Unit
) {
    val appointments = when (selectedTab) {
        AppointmentsContract.AppointmentTab.UPCOMING -> upcomingAppointments
        AppointmentsContract.AppointmentTab.PAST -> pastAppointments
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        items(appointments) { appointment ->
            AppointmentCard(
                appointment = appointment,
                onClick = { onAppointmentClick(appointment) }
            )
        }
    }
}

@Composable
private fun AppointmentCard(
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

@Preview
@Composable
private fun EmptyAppointmentsScreenPreview() {
    AynaAppTheme {
        EmptyAppointmentsContent(onSearchSalonsClick = {})
    }
}

@Preview
@Composable
private fun AppointmentsScreenPreview() {
    AynaAppTheme {
        Surface {
            // Preview placeholder - would normally show appointments content
            EmptyAppointmentsContent(onSearchSalonsClick = {})
        }
    }
}
