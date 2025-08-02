package com.techtactoe.ayna.presentation.ui.screens.appointments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.techtactoe.ayna.designsystem.ErrorContent
import com.techtactoe.ayna.designsystem.LoadingContent
import com.techtactoe.ayna.presentation.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.ui.screens.appointments.component.EmptyAppointmentsContent
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
    if (uiState.navigateToSearch) {
        navController.navigate("explore")
        onEvent(AppointmentsContract.UiEvent.OnNavigationHandled(AppointmentsContract.NavigationReset.SEARCH))
    }

    uiState.navigateToAppointmentDetail?.let { appointmentId ->
        navController.navigate("appointment_detail/$appointmentId")
        onEvent(AppointmentsContract.UiEvent.OnNavigationHandled(AppointmentsContract.NavigationReset.APPOINTMENT_DETAIL))
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
                            onAppointmentClick = { appointment ->
                                onEvent(
                                    AppointmentsContract.UiEvent.OnAppointmentClicked(
                                        appointment.id
                                    )
                                )
                            }
                        )
                    }
                }
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