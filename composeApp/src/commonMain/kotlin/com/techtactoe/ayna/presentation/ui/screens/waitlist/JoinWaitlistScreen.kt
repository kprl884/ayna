package com.techtactoe.ayna.presentation.ui.screens.waitlist

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.presentation.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.theme.Spacing
import com.techtactoe.ayna.presentation.theme.brandPurple
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen for joining waitlist when no appointments are available
 * Following the golden standard MVVM pattern
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinWaitlistScreen(
    uiState: JoinWaitlistContract.UiState,
    onEvent: (JoinWaitlistContract.UiEvent) -> Unit,
    salonId: String,
    serviceId: String,
    onNavigateBack: () -> Unit,
    onNavigateClose: () -> Unit,
    onNavigateToContinue: () -> Unit,
    onNavigateToBooking: () -> Unit,
    onNavigateToSelectTime: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Initialize with salon and service IDs
    LaunchedEffect(salonId, serviceId) {
        onEvent(JoinWaitlistContract.UiEvent.OnInitialize(salonId, serviceId))
    }

    // Handle navigation effects
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            onNavigateBack()
            onEvent(JoinWaitlistContract.UiEvent.OnNavigationHandled(JoinWaitlistContract.NavigationReset.BACK))
        }
    }

    LaunchedEffect(uiState.navigateToClose) {
        if (uiState.navigateToClose) {
            onNavigateClose()
            onEvent(JoinWaitlistContract.UiEvent.OnNavigationHandled(JoinWaitlistContract.NavigationReset.CLOSE))
        }
    }

    LaunchedEffect(uiState.navigateToBooking) {
        if (uiState.navigateToBooking) {
            onNavigateToBooking()
            onEvent(JoinWaitlistContract.UiEvent.OnNavigationHandled(JoinWaitlistContract.NavigationReset.BOOKING))
        }
    }

    LaunchedEffect(uiState.navigateToSelectTime) {
        if (uiState.navigateToSelectTime) {
            onNavigateToSelectTime()
            onEvent(JoinWaitlistContract.UiEvent.OnNavigationHandled(JoinWaitlistContract.NavigationReset.SELECT_TIME))
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToContinue()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onEvent(JoinWaitlistContract.UiEvent.OnBackClick) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(JoinWaitlistContract.UiEvent.OnCloseClick) }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "free",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "1 service • 30 mins",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = {
                            if (uiState.hasAvailableSlots) {
                                onEvent(JoinWaitlistContract.UiEvent.OnBookNow)
                            } else {
                                onEvent(JoinWaitlistContract.UiEvent.OnJoinWaitlist)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        enabled = !uiState.isSubmitting,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(Spacing.md),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = Spacing.xs / 2
                            )
                        } else {
                            Text(
                                "Continue",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.md)
        ) {
            Text(
                text = "Join the waitlist",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = Spacing.sm)
            )

            Text(
                text = "Select your preferred dates and time. We'll notify you if a time slot becomes available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = Spacing.xl)
            )

            // Date and Time selectors
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Date",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = Spacing.sm)
                    )

                    DateSelector(
                        selectedDate = uiState.formattedDate,
                        onClick = { /* TODO: Open date picker */ }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Time",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = Spacing.sm)
                    )

                    TimeRangeSelector(
                        selectedTimeRange = uiState.selectedTimeRange,
                        options = uiState.timeRangeOptions,
                        onOptionSelected = { timeRange ->
                            onEvent(JoinWaitlistContract.UiEvent.OnTimeRangeSelected(timeRange))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.lg))

            // Add another time option
            OutlinedButton(
                onClick = { onEvent(JoinWaitlistContract.UiEvent.OnAddAnotherTime) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = Spacing.sm)
                )
                Text(
                    "Add another time",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(Spacing.xl))

            // Available slots notification
            if (uiState.hasAvailableSlots) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(Spacing.md)
                    ) {
                        Text(
                            text = uiState.availableSlotsMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = Spacing.md)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = { onEvent(JoinWaitlistContract.UiEvent.OnBookNow) }) {
                                Text(
                                    text = "Book now →",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.lg))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Changed your mind? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    TextButton(
                        onClick = { onEvent(JoinWaitlistContract.UiEvent.OnSeeAvailableTimes) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "See available times to book",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.brandPurple
                        )
                    }
                }
            }

            // Error handling
            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(Spacing.md))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = MaterialTheme.shapes.medium,
                    onClick = { onEvent(JoinWaitlistContract.UiEvent.OnClearError) }
                ) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(Spacing.md)
                    )
                }
            }
        }
    }
}

@Composable
private fun DateSelector(
    selectedDate: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.xxxl - Spacing.sm)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                MaterialTheme.shapes.small
            )
            .clickable { onClick() }
            .padding(horizontal = Spacing.md),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedDate,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select date",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TimeRangeSelector(
    selectedTimeRange: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing.xxxl - Spacing.sm)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    MaterialTheme.shapes.small
                )
                .clickable { expanded = true }
                .padding(horizontal = Spacing.md),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedTimeRange,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select time range",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun JoinWaitlistScreenPreview() {
    AynaAppTheme {
        // Preview implementation would go here
    }
}
