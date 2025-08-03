package com.techtactoe.ayna.presentation.ui.screens.map.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.AnimationDuration
import com.techtactoe.ayna.designsystem.theme.Elevation
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.SalonMapCard
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class SalonMapCardViewState(
    val salon: SalonMapCard?,
    val isVisible: Boolean = false,
    val isFavorite: Boolean = false
)

@Composable
fun SalonMapCard(
    viewState: SalonMapCardViewState,
    onSalonClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = viewState.isVisible && viewState.salon != null,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = AnimationDuration.normal),
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = AnimationDuration.normal),
            targetOffsetY = { it }
        )
    ) {
        viewState.salon?.let { salon ->
            Card(
                modifier = modifier
                    .width(280.dp)
                    .clickable { onSalonClick() },
                shape = AynaShapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = Elevation.lg),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(Spacing.medium)
                ) {
                    // Image section with favorite button overlay
                    Box {
                        AsyncImage(
                            model = salon.imageUrl,
                            contentDescription = "${salon.name} image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(AynaShapes.medium)
                        )
                        
                        // Favorite button with glassmorphism effect
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(Spacing.small)
                                .size(32.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = onFavoriteClick,
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = if (viewState.isFavorite) {
                                        Icons.Default.Favorite
                                    } else {
                                        Icons.Outlined.FavoriteBorder
                                    },
                                    contentDescription = "Favorite",
                                    tint = if (viewState.isFavorite) {
                                        Color.Red
                                    } else {
                                        Color.White
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    
                    // Salon info section
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.small)
                    ) {
                        // Salon name and distance
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = salon.name,
                                style = AynaTypography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            
                            Text(
                                text = salon.distance,
                                style = AynaTypography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        // Location
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            
                            Text(
                                text = salon.location,
                                style = AynaTypography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        // Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
                        ) {
                            Text(
                                text = "⭐",
                                style = AynaTypography.labelMedium
                            )
                            
                            Text(
                                text = "${salon.rating}",
                                style = AynaTypography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Text(
                                text = "(${salon.reviewCount})",
                                style = AynaTypography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
fun SalonMapCardPreview() {
    val mockViewState = SalonMapCardViewState(
        salon = SalonMapCard(
            id = "1",
            name = "Hair Avenue",
            address = "Lakewood, California",
            location = "Lakewood, California",
            rating = 4.7,
            reviewCount = 312,
            distance = "2 km",
            imageUrl = "https://cdn.builder.io/api/v1/image/assets%2F5b75d307a1554817a72557f83cf3c781%2F7e84350429ab463c8d2a9981fcc3cce8?format=webp&width=800"
        ),
        isVisible = true,
        isFavorite = false
    )
    
    MaterialTheme {
        SalonMapCard(
            viewState = mockViewState
        )
    }
}
