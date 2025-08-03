package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.NotificationUiState
import com.techtactoe.ayna.designsystem.theme.AynaColors
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

@Preview
@Composable
fun UserHeaderPreview() {
    UserHeader(
        userName = "John",
        notificationState = NotificationUiState(hasUnreadNotifications = true, unreadCount = 3)
    )
}
