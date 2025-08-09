package com.techtactoe.ayna.presentation.ui.screens.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.techtactoe.ayna.common.designsystem.theme.AynaShapes
import com.techtactoe.ayna.common.designsystem.theme.Elevation
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.Venue
import com.techtactoe.ayna.domain.model.VenueService
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Refactored VenueCard component following SOLID principles and design system integration
 * - Single Responsibility: Only handles venue display
 * - Open/Closed: Extensible through ViewState pattern
 * - Performance optimized with @Stable annotations
 */

@Stable
data class VenueCardViewState(
    val venue: Venue,
    val showFullServices: Boolean = false
)

@Composable
fun VenueCardRefactored(
    viewState: VenueCardViewState,
    onVenueClick: () -> Unit,
    onSeeMoreClick: () -> Unit,
    onBookmarkClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onVenueClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.sm),
        shape = AynaShapes.medium
    ) {
        Column {
            // Image carousel section
            VenueImageCarouselRefactored(
                images = viewState.venue.images,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            // Venue information section
            VenueInfoSection(
                venue = viewState.venue,
                showFullServices = viewState.showFullServices,
                onSeeMoreClick = onSeeMoreClick,
                modifier = Modifier.padding(Spacing.medium)
            )
        }
    }
}

@Composable
private fun VenueImageCarouselRefactored(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    if (images.isEmpty()) {
        EmptyImagePlaceholder(modifier = modifier)
        return
    }

    Box(modifier = modifier) {
        val pagerState = rememberPagerState(pageCount = { images.size })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Venue image ${page + 1}",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(AynaShapes.medium),
                contentScale = ContentScale.Crop
            )
        }

        // Page indicators
        if (images.size > 1) {
            PageIndicators(
                currentPage = pagerState.currentPage,
                pageCount = images.size,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Spacing.medium)
            )
        }
    }
}

@Composable
private fun EmptyImagePlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No Image",
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PageIndicators(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = if (index == currentPage) {
                            Color.White
                        } else {
                            Color.White.copy(alpha = 0.5f)
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun VenueInfoSection(
    venue: Venue,
    showFullServices: Boolean,
    onSeeMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        // Venue name
        VenueName(
            name = venue.name,
            modifier = Modifier.fillMaxWidth()
        )

        // Rating and location
        VenueRatingAndLocation(
            rating = venue.rating,
            reviewCount = venue.reviewCount,
            district = venue.district,
            city = venue.city
        )

        // Services
        VenueServices(
            services = venue.services,
            showFullServices = showFullServices,
            onSeeMoreClick = onSeeMoreClick
        )
    }
}

@Composable
private fun VenueName(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = name,
        style = AynaTypography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
private fun VenueRatingAndLocation(
    rating: Double,
    reviewCount: Int,
    district: String,
    city: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
    ) {
        // Rating
        VenueRating(
            rating = rating,
            reviewCount = reviewCount
        )

        // Location
        Text(
            text = "$district, $city",
            style = AynaTypography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun VenueRating(
    rating: Double,
    reviewCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        Text(
            text = rating.toString(),
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        StarRating(rating = rating)

        Text(
            text = "($reviewCount)",
            style = AynaTypography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (index < rating.toInt()) {
                    Color(0xFFFFB000) // Consider moving to design system
                } else {
                    MaterialTheme.colorScheme.outline
                },
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Composable
private fun VenueServices(
    services: List<VenueService>,
    showFullServices: Boolean,
    onSeeMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        val servicesToShow = if (showFullServices) services else services.take(3)

        servicesToShow.forEach { service ->
            VenueServiceItem(service = service)
        }

        // See more link
        if (services.size > 3 || services.isNotEmpty()) {
            TextButton(
                onClick = onSeeMoreClick,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.offset(x = (-12).dp)
            ) {
                Text(
                    text = "See more",
                    style = AynaTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun VenueServiceItem(
    service: VenueService,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
        ) {
            Text(
                text = service.name,
                style = AynaTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = formatDuration(service.duration),
                style = AynaTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = if (service.price > 0) "TRY ${service.price / 100}" else "free",
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun formatDuration(minutes: Int): String {
    return when {
        minutes < 60 -> "$minutes mins"
        minutes % 60 == 0 -> "${minutes / 60} hr"
        else -> "${minutes / 60} hr, ${minutes % 60} mins"
    }
}

@Preview
@Composable
private fun VenueCardRefactoredPreview() {
    MaterialTheme {
        VenueCardRefactored(
            viewState = VenueCardViewState(
                venue = Venue(
                    id = "1",
                    name = "Hair Avenue",
                    reviewCount = 312,
                    district = "Kadıköy",
                    city = "Istanbul",
                    images = listOf("https://example.com/image1.jpg"),
                    rating = 4.5,
                    services = listOf()
                )
            ),
            onVenueClick = {},
            onSeeMoreClick = {}
        )
    }
}
