package com.techtactoe.ayna.presentation.ui.screens.explore.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.BorderThickness
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.ExploreFiltersUiModel
import com.techtactoe.ayna.domain.model.SortOption
import com.techtactoe.ayna.domain.model.VenueType
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Refactored FilterChipBar following atomic design principles
 * Each chip is a reusable atomic component with proper design system integration
 */

@Stable
data class FilterChipBarViewState(
    val filters: ExploreFiltersUiModel,
    val activeFiltersCount: Int = 0
)

@Composable
fun FilterChipBarRefactored(
    viewState: FilterChipBarViewState,
    onFiltersClick: () -> Unit,
    onSortClick: () -> Unit,
    onPriceClick: () -> Unit,
    onTypeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        // All filters chip (atomic component)
        FilterChipAll(
            isSelected = viewState.activeFiltersCount > 0,
            onClick = onFiltersClick
        )

        // Sort chip (atomic component)
        FilterChipSort(
            sortOption = viewState.filters.sortOption,
            isSelected = viewState.filters.sortOption != SortOption.RECOMMENDED,
            onClick = onSortClick
        )

        // Price chip (atomic component)
        FilterChipPrice(
            maxPrice = viewState.filters.priceRange.max,
            isSelected = viewState.filters.priceRange.max < 30000,
            onClick = onPriceClick
        )

        // Type chip (atomic component)
        FilterChipType(
            venueType = viewState.filters.venueType,
            isSelected = viewState.filters.venueType != VenueType.EVERYONE,
            onClick = onTypeClick
        )
    }
}

/**
 * Atomic Component: All Filters Chip
 * Single responsibility: Show filters icon and handle all filters click
 */
@Composable
private fun FilterChipAll(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseFilterChip(
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "All Filters",
            modifier = Modifier.size(18.dp)
        )
    }
}

/**
 * Atomic Component: Sort Filter Chip
 * Single responsibility: Display and handle sort options
 */
@Composable
private fun FilterChipSort(
    sortOption: SortOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChipWithDropdown(
        text = getSortDisplayText(sortOption),
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier
    )
}

/**
 * Atomic Component: Price Filter Chip
 * Single responsibility: Display and handle price range
 */
@Composable
private fun FilterChipPrice(
    maxPrice: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChipWithDropdown(
        text = getPriceDisplayText(maxPrice),
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier
    )
}

/**
 * Atomic Component: Type Filter Chip
 * Single responsibility: Display and handle venue types
 */
@Composable
private fun FilterChipType(
    venueType: VenueType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChipWithDropdown(
        text = getTypeDisplayText(venueType),
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier
    )
}

/**
 * Base Component: Filter Chip with dropdown indicator
 * Reusable base for chips that need dropdown arrows
 */
@Composable
private fun FilterChipWithDropdown(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseFilterChip(
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
        ) {
            Text(
                text = text,
                style = AynaTypography.labelMedium
            )

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = "Dropdown",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Base Component: Generic Filter Chip
 * Foundation component for all filter chips
 */
@Composable
private fun BaseFilterChip(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = content,
        modifier = modifier.height(36.dp),
        shape = AynaShapes.large,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = MaterialTheme.colorScheme.outline,
            selectedBorderColor = MaterialTheme.colorScheme.primary,
            borderWidth = BorderThickness.extraSmall,
            selectedBorderWidth = BorderThickness.extraSmall
        )
    )
}

@Preview
@Composable
private fun FilterChipBarRefactoredPreview() {
    MaterialTheme {
        FilterChipBarRefactored(
            viewState = FilterChipBarViewState(
                filters = ExploreFiltersUiModel(
                    sortOption = SortOption.TOP_RATED,
                    venueType = VenueType.FEMALE_ONLY
                ),
                activeFiltersCount = 2
            ),
            onFiltersClick = {},
            onSortClick = {},
            onPriceClick = {},
            onTypeClick = {}
        )
    }
}
