package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.designsystem.icon.IconWithImageVector
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.domain.model.NotificationUiState

@Composable
fun NotificationIcon(
    notificationState: NotificationUiState,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        finishedListener = { isPressed = false }
    )

    IconButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .scale(scale)
            .padding(horizontal = Spacing.medium)
    ) {
        BadgedBox(
            badge = {
                if (notificationState.hasUnreadNotifications) {
                    Badge(
                        modifier = Modifier.padding(Spacing.small).size(8.dp),
                        containerColor = MaterialTheme.colorScheme.error
                    )
                }
            }
        ) {
            IconWithImageVector(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications"
            )
        }
    }
}