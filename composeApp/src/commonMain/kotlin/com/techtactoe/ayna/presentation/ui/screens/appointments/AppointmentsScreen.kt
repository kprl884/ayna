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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techtactoe.ayna.designsystem.ErrorContent
import com.techtactoe.ayna.designsystem.LoadingContent
import com.techtactoe.ayna.presentation.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.theme.Spacing
import com.techtactoe.ayna.presentation.ui.screens.appointments.component.PurpleCalendarIcon
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
                            AppointmentsContract.AppointmentTab.entries.forEach { tab ->
                                Tab(
                                    selected = uiState.selectedTab == tab,
                                    onClick = {
                                        onEvent(
                                            AppointmentsContract.UiEvent.OnTabSelected(
                                                tab
                                            )
                                        )
                                    },
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
                            onAppointmentClick = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyAppointmentsContent(
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EmptyAppointmentsContent(onSearchSalonsClick = {})
            }
        }
    }
}

@Preview
@Composable
private fun PurpleCalendarIconPreview() {
    AynaAppTheme {
        Surface(
            modifier = Modifier.padding(Spacing.lg)
        ) {
            PurpleCalendarIcon()
        }
    }
}
