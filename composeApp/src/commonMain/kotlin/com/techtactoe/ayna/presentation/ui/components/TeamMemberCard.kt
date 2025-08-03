package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.CornerRadius
import com.techtactoe.ayna.designsystem.theme.Elevation
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.theme.StringResources
import com.techtactoe.ayna.designsystem.typography.AynaTypography

@Stable
data class TeamMemberCardViewState(
    val id: String,
    val name: String,
    val role: String,
    val imageUrl: String?,
    val rating: Double
)

@Composable
fun TeamMemberCard(
    viewState: TeamMemberCardViewState,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = AynaShapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.xs),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            // Avatar
            if (viewState.imageUrl != null) {
                AsyncImage(
                    model = viewState.imageUrl,
                    contentDescription = "${viewState.name} profile photo",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(AynaShapes.large)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(AynaShapes.large)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        style = AynaTypography.headlineMedium
                    )
                }
            }

            // Rating
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.extraSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${viewState.rating}",
                    style = AynaTypography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "⭐",
                    style = AynaTypography.labelMedium
                )
            }

            // Name
            Text(
                text = viewState.name,
                style = AynaTypography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            // Role
            Text(
                text = viewState.role,
                style = AynaTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // View Profile Button
            TextButton(
                onClick = onClick,
                shape = AynaShapes.small
            ) {
                Text(
                    text = StringResources.view_profile_button,
                    style = AynaTypography.labelMedium
                )
            }
        }
    }
}
