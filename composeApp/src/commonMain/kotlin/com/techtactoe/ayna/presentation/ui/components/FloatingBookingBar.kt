package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.designsystem.button.PrimaryButton
import com.techtactoe.ayna.designsystem.theme.AnimationDuration
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.Elevation
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.theme.StringResources
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class FloatingBookingBarViewState(
    val serviceCount: Int,
    val isVisible: Boolean = true,
    val isEnabled: Boolean = true
)

@Composable
fun FloatingBookingBar(
    viewState: FloatingBookingBarViewState,
    onBookNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = viewState.isVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = AnimationDuration.normal)
        ) { it },
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = AnimationDuration.normal)
        ) { it }
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            shape = AynaShapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = Elevation.md),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${viewState.serviceCount} services available",
                    style = AynaTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                PrimaryButton(
                    text = StringResources.book_now_button,
                    onClick = onBookNowClick,
                    enabled = viewState.isEnabled
                )
            }
        }
    }
}

@Preview
@Composable
fun FloatingBookingBarPreview() {
    val mockViewState = FloatingBookingBarViewState(
        serviceCount = 51,
        isVisible = true,
        isEnabled = true
    )

    MaterialTheme {
        FloatingBookingBar(
            viewState = mockViewState,
            onBookNowClick = {}
        )
    }
}
