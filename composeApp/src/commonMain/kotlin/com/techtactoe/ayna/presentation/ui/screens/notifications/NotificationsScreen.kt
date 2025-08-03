package com.techtactoe.ayna.presentation.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techtactoe.ayna.designsystem.LoadingContent
import com.techtactoe.ayna.domain.model.NotificationItem
import com.techtactoe.ayna.domain.model.NotificationType
import com.techtactoe.ayna.presentation.theme.Spacing
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
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Handle navigation effects
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            navController.navigateUp()
            onEvent(NotificationsContract.UiEvent.OnNavigationHandled(NotificationsContract.NavigationReset.BACK))
        }
    }

    LaunchedEffect(uiState.navigateToRoute) {
        uiState.navigateToRoute?.let { route ->
            navController.navigate(route)
            onEvent(NotificationsContract.UiEvent.OnNavigationHandled(NotificationsContract.NavigationReset.ROUTE))
        }
    }

    // Initialize when screen loads
    LaunchedEffect(Unit) {
        onEvent(NotificationsContract.UiEvent.OnInitialize)
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
                    IconButton(onClick = { onEvent(NotificationsContract.UiEvent.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Mark all as read button
                    TextButton(
                        onClick = { onEvent(NotificationsContract.UiEvent.OnMarkAllAsRead) }
                    ) {
                        Text(
                            "Mark all as read",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // Do not disturb toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = Spacing.sm)
                    ) {
                        Text(
                            "Do not disturb",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(end = Spacing.xs)
                        )
                        Switch(
                            checked = uiState.doNotDisturbEnabled,
                            onCheckedChange = { 
                                onEvent(NotificationsContract.UiEvent.OnToggleDoNotDisturb(it))
                            }
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
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationsContent(
    notifications: List<NotificationItem>,
    onNotificationClick: (NotificationItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        items(notifications) { notification ->
            NotificationCard(
                notification = notification,
                onClick = { onNotificationClick(notification) }
            )
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            // Read/Unread indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (notification.isRead) 
                            MaterialTheme.colorScheme.outline
                        else 
                            MaterialTheme.colorScheme.primary
                    )
                    .align(Alignment.Top)
            )
            
            // Content area
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                // Primary text
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Secondary text (timestamp)
                Text(
                    text = notification.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Optional action text
                notification.actionText?.let { actionText ->
                    Text(
                        text = actionText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onClick() }
                            .padding(top = Spacing.xs)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyNotificationsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No notifications",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.md)
        )
        
        Text(
            text = "You're all caught up!\nNew notifications will appear here.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
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
