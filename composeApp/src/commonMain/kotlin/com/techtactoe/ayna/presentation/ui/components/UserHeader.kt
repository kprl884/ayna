package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.NotificationUiState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class UserHeaderViewState(
    val userName: String,
    val notificationState: NotificationUiState = NotificationUiState()
)

@Composable
fun UserHeader(
    viewState: UserHeaderViewState,
    onNotificationClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Hey, ${viewState.userName}",
            style = AynaTypography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        /*
        todo: notification feature will update in future

            NotificationIcon(
                notificationState = viewState.notificationState,
                onClick = onNotificationClick
            )
         */
    }
}

@Preview
@Composable
fun UserHeaderPreview() {
    UserHeader(
        viewState = UserHeaderViewState(
            userName = "John",
            notificationState = NotificationUiState(hasUnreadNotifications = true, unreadCount = 3)
        )
    )
}
