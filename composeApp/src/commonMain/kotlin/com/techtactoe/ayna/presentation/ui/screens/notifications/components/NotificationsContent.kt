package com.techtactoe.ayna.presentation.ui.screens.notifications.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.domain.model.NotificationItem
import com.techtactoe.ayna.presentation.ui.screens.notifications.NotificationsContract

@Composable
fun NotificationsContent(
    notifications: List<NotificationItem>,
    onNotificationClick: (NotificationItem) -> Unit,
    uiState: NotificationsContract.UiState,
    onEvent: (NotificationsContract.UiEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        item {

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
                modifier = Modifier.padding(end = Spacing.small)
            ) {
                Text(
                    "Do not disturb",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = Spacing.extraSmall)
                )
                Switch(
                    checked = uiState.doNotDisturbEnabled,
                    colors = androidx.compose.material3.SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    ),
                    onCheckedChange = {
                        onEvent(NotificationsContract.UiEvent.OnToggleDoNotDisturb(it))
                    }
                )
            }
        }
        items(notifications) { notification ->
            NotificationCard(
                notification = notification,
                onClick = { onNotificationClick(notification) }
            )
        }
    }
}