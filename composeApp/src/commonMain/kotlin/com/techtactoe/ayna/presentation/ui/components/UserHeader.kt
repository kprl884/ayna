package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import coil3.compose.AsyncImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.NotificationUiState
import com.techtactoe.ayna.presentation.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserHeader(
    userName: String,
    notificationState: NotificationUiState = NotificationUiState(),
    onNotificationClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Hey, $userName",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = AynaColors.PrimaryText
        )

        NotificationIcon(
            notificationState = notificationState,
            onClick = onNotificationClick
        )
    }
}

@Composable
private fun NotificationIcon(
    notificationState: NotificationUiState,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        finishedListener = { isPressed = false }
    )

    BadgedBox(
        badge = {
            if (notificationState.hasUnreadNotifications) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
    ) {
        IconButton(
            onClick = {
                isPressed = true
                onClick()
            },
            modifier = Modifier
                .scale(scale)
                .padding(horizontal = 16.dp)
        ) {
            AsyncImage(
                model = if (notificationState.hasUnreadNotifications) {
                    "https://cdn.builder.io/api/v1/image/assets%2F5b75d307a1554817a72557f83cf3c781%2Ff330551911d44ccaa380526ffdbf7c71?format=webp&width=800"
                } else {
                    "https://cdn.builder.io/api/v1/image/assets%2F5b75d307a1554817a72557f83cf3c781%2Fc44a4871c4dd44f68c0fa011cab37cd9?format=webp&width=800"
                },
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun UserHeaderPreview() {
    UserHeader(
        userName = "John",
        notificationState = NotificationUiState(hasUnreadNotifications = true, unreadCount = 3)
    )
}
