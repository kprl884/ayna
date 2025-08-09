package com.techtactoe.ayna.presentation.ui.screens.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.techtactoe.ayna.common.designsystem.component.content.LoadingContent
import com.techtactoe.ayna.domain.model.NotificationItem
import com.techtactoe.ayna.domain.model.NotificationType
import com.techtactoe.ayna.presentation.ui.screens.notifications.components.EmptyNotificationsContent
import com.techtactoe.ayna.presentation.ui.screens.notifications.components.NotificationCard
import com.techtactoe.ayna.presentation.ui.screens.notifications.components.NotificationsContent
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen displaying user notifications with clean card layout
 * Following the golden standard MVVM pattern
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    uiState: NotificationsContract.UiState,
    onEvent: (NotificationsContract.UiEvent) -> Unit,
    onBackClick: () -> Unit,
    navController: NavController,
    viewModel: NotificationsViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { navigationEvent ->
            when (navigationEvent) {
                is NotificationsContract.NavigationEvent.NavigateBack -> {
                    navController.navigateUp()
                }

                is NotificationsContract.NavigationEvent.NavigateToScreen -> {
                    navController.navigate(navigationEvent.screen)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Notifications",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
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

                uiState.notifications.isEmpty() -> {
                    EmptyNotificationsContent()
                }

                else -> {
                    NotificationsContent(
                        notifications = uiState.notifications,
                        onNotificationClick = { notification ->
                            onEvent(NotificationsContract.UiEvent.OnNotificationClick(notification))
                        },
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotificationsScreenPreview() {
    MaterialTheme {
        NotificationCard(
            notification = NotificationItem(
                id = "1",
                message = "You have an appointment at The Galleria Hair Salon at 8:00am today",
                timestamp = "Just now",
                isRead = false,
                type = NotificationType.APPOINTMENT_REMINDER
            ),
            onClick = {}
        )
    }
}
