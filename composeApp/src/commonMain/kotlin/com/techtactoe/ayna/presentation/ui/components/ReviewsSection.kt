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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.domain.model.Review
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.Elevation
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.theme.StringResources
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class ReviewsSectionViewState(
    val reviews: List<Review>,
    val overallRating: Double,
    val reviewCount: Int
)

@Composable
fun ReviewsSection(
    viewState: ReviewsSectionViewState,
    onSeeAllReviewsClick: () -> Unit = {},
    onReadMoreClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        SectionHeader(
            title = StringResources.reviews_text,
            actionText = "See all",
            onActionClick = onSeeAllReviewsClick
        )

        // Overall rating
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            // Star rating
            repeat(5) { index ->
                Text(
                    text = if (index < viewState.overallRating.toInt()) "⭐" else "☆",
                    style = AynaTypography.titleMedium
                )
            }

            Text(
                modifier = Modifier.clickable { onSeeAllReviewsClick() },
                text = "${viewState.overallRating} (${viewState.reviewCount} ${StringResources.rating_text})",
                style = AynaTypography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Individual reviews
        viewState.reviews.take(3).forEach { review ->
            ReviewCard(
                review = review,
                onReadMoreClick = { onReadMoreClick(review.id) }
            )
        }
    }
}

@Composable
private fun ReviewCard(
    review: Review,
    onReadMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AynaShapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.xs),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            // User avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = review.userInitials,
                    style = AynaTypography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
            ) {
                // User name and date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = review.userName,
                        style = AynaTypography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = review.date,
                        style = AynaTypography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Star rating
                Row {
                    repeat(5) { index ->
                        Text(
                            text = if (index < review.rating) "⭐" else "☆",
                            style = AynaTypography.labelMedium
                        )
                    }
                }

                // Review comment
                Text(
                    text = review.comment,
                    style = AynaTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Read more link (if needed)
                if (review.comment.length > 100) {
                    Text(
                        text = "Read more",
                        style = AynaTypography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onReadMoreClick() }
                    )
                }
            }
        }
    }
    }
}

@Preview
@Composable
fun ReviewsSectionPreview() {
    val mockViewState = ReviewsSectionViewState(
        reviews = listOf(
            Review(
                id = "1",
                userName = "Monica T",
                userInitials = "MT",
                date = "Sat, Jul 19, 2025 at 10:28 PM",
                rating = 5,
                comment = "Only the best! Much appreciated 😊"
            ),
            Review(
                id = "2",
                userName = "Froso P",
                userInitials = "FP",
                date = "Sat, Jul 19, 2025 at 12:52 PM",
                rating = 5,
                comment = "Absolutely excellent service! The environment is welcoming, stylish, and relaxing. Special thanks to the team for their professionalism."
            )
        ),
        overallRating = 5.0,
        reviewCount = 3645
    )

    MaterialTheme {
        ReviewsSection(
            viewState = mockViewState
        )
    }
}
