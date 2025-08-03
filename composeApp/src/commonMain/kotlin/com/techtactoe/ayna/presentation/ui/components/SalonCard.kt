package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.domain.model.DayOfWeek
import com.techtactoe.ayna.domain.model.Employee
import com.techtactoe.ayna.domain.model.Location
import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.model.Service
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.Elevation
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.theme.StringResources
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class SalonCardViewState(
    val id: String,
    val name: String,
    val address: String,
    val rating: Double,
    val reviewCount: Int,
    val imageUrl: String?,
    val tags: List<String>,
    val isNew: Boolean = false
)

@Composable
fun SalonCard(
    viewState: SalonCardViewState,
    onSalonClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(280.dp)
            .clickable { onSalonClick(viewState.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.sm),
        shape = AynaShapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(AynaShapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🪒",
                    style = AynaTypography.displaySmall
                )
            }

            Column(
                modifier = Modifier.padding(Spacing.medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
            ) {
                // Salon name and new badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = viewState.name,
                        style = AynaTypography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (viewState.isNew) {
                        Text(
                            text = StringResources.new_text,
                            style = AynaTypography.labelMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    shape = AynaShapes.small
                                )
                                .padding(
                                    horizontal = Spacing.small,
                                    vertical = Spacing.extraSmall
                                )
                        )
                    }
                }

                // Rating and review count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
                ) {
                    Text(
                        text = "${viewState.rating} ⭐",
                        style = AynaTypography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "(${viewState.reviewCount} ${StringResources.rating_text})",
                        style = AynaTypography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Address
                Text(
                    text = viewState.address,
                    style = AynaTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Category tags
                if (viewState.tags.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
                    ) {
                        viewState.tags.take(2).forEach { tag ->
                            Text(
                                text = tag,
                                style = AynaTypography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        shape = AynaShapes.small
                                    )
                                    .padding(
                                        horizontal = Spacing.small,
                                        vertical = Spacing.extraSmall
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SalonCardPreview() {
    val mockViewState = SalonCardViewState(
        id = "1",
        name = "Barbershop Dolapdere",
        address = "Dolapdere Mahallesi, 34384 Istanbul",
        rating = 5.0,
        reviewCount = 69,
        imageUrl = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=800",
        tags = listOf(StringResources.barber_text, StringResources.popular_text),
        isNew = true
    )

    SalonCard(viewState = mockViewState)
}
