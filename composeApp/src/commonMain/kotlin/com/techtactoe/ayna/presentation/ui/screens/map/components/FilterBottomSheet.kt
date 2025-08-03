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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.designsystem.button.PrimaryButton
import com.techtactoe.ayna.designsystem.theme.AnimationDuration
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.Elevation
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.MapFilterState
import com.techtactoe.ayna.domain.model.ServiceType
import com.techtactoe.ayna.domain.model.SortOptionMap
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class FilterBottomSheetViewState(
    val isVisible: Boolean = false,
    val filterState: MapFilterState = MapFilterState()
)

@Composable
fun FilterBottomSheet(
    viewState: FilterBottomSheetViewState,
    onFilterChange: (MapFilterState) -> Unit = {},
    onApply: () -> Unit = {},
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = viewState.isVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = AnimationDuration.normal),
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = AnimationDuration.normal),
            targetOffsetY = { it }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { onDismiss() }
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .clickable(enabled = false) { }, // Prevent card clicks from dismissing
                shape = AynaShapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = Elevation.xl),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(Spacing.large)
                ) {
                    // Bottom sheet indicator
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                            .align(Alignment.CenterHorizontally)
                    )

                    // Title
                    Text(
                        text = "Filters",
                        style = AynaTypography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Sort by section
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
                    ) {
                        Text(
                            text = "Sort by",
                            style = AynaTypography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing.small)
                        ) {
                            SortOptionMap.entries.forEach { option ->
                                SortOptionItem(
                                    option = option,
                                    isSelected = viewState.filterState.sortBy == option,
                                    onClick = {
                                        onFilterChange(
                                            viewState.filterState.copy(sortBy = option)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // Service Availability section
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
                    ) {
                        Text(
                            text = "Service Availability",
                            style = AynaTypography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
                        ) {
                            items(ServiceType.values()) { serviceType ->
                                ServiceTypeChip(
                                    serviceType = serviceType,
                                    isSelected = viewState.filterState.serviceType == serviceType,
                                    onClick = {
                                        onFilterChange(
                                            viewState.filterState.copy(serviceType = serviceType)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(Spacing.small))

                    // Apply button
                    PrimaryButton(
                        text = "Apply",
                        onClick = onApply,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun SortOptionItem(
    option: SortOptionMap,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = Spacing.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Text(
            text = when (option) {
                SortOptionMap.NEAREST -> "Nearest"
                SortOptionMap.TOP_RATED -> "Top Rated"
                SortOptionMap.PRICE_LOW_TO_HIGH -> "Price low to high"
                SortOptionMap.PRICE_HIGH_TO_LOW -> "Price high to low"
            },
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ServiceTypeChip(
    serviceType: ServiceType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(AynaShapes.large)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(
                horizontal = Spacing.medium,
                vertical = Spacing.small
            )
    ) {
        Text(
            text = when (serviceType) {
                ServiceType.ALL -> "All"
                ServiceType.MALE_ONLY -> "Male only"
                ServiceType.FEMALE_ONLY -> "Female Only"
            },
            style = AynaTypography.labelMedium,
            color = textColor
        )
    }
}

@Preview
@Composable
fun FilterBottomSheetPreview() {
    val mockViewState = FilterBottomSheetViewState(
        isVisible = true,
        filterState = MapFilterState()
    )

    MaterialTheme {
        FilterBottomSheet(
            viewState = mockViewState
        )
    }
}
