package com.techtactoe.ayna.presentation.ui.screens.appointments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.techtactoe.ayna.designsystem.ErrorContent
import com.techtactoe.ayna.designsystem.LoadingContent
import com.techtactoe.ayna.designsystem.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.ui.screens.appointments.component.EmptyAppointmentsContent
import com.techtactoe.ayna.presentation.ui.screens.appointments.model.AppointmentTab
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen displaying user appointments following the golden standard MVVM pattern
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    uiState: AppointmentsContract.UiState,
    onEvent: (AppointmentsContract.UiEvent) -> Unit,
    modifier: Modifier = Modifier,
    navigateToAppointmentDetail: (String) -> Unit,
    navigateToSearch: () -> Unit,
) {
    if (uiState.navigateToSearch) {
        navigateToSearch()
        onEvent(AppointmentsContract.UiEvent.OnClearNavigateState)
    }

    uiState.navigateToAppointmentDetail?.let { appointmentId ->
        navigateToAppointmentDetail(appointmentId)
        onEvent(AppointmentsContract.UiEvent.OnClearNavigateState)
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
                            AppointmentTab.entries.forEach { tab ->
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
                                                AppointmentTab.UPCOMING -> "Upcoming"
                                                AppointmentTab.PAST -> "Past"
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