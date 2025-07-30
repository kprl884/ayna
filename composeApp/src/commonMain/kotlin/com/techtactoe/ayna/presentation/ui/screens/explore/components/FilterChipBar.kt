package com.techtactoe.ayna.presentation.ui.screens.explore.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.ExploreFilters
import com.techtactoe.ayna.domain.model.SortOption
import com.techtactoe.ayna.domain.model.VenueType
import com.techtactoe.ayna.presentation.theme.AynaAppTheme

/**
 * Filter chip bar component showing filter options
 * Designed to be sticky below the search bar
 */
@Composable
fun FilterChipBar(
    filters: ExploreFilters,
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
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // All filters chip
        FilterChip(
            icon = Icons.Outlined.Tune,
            text = "",
            isSelected = false,
            onClick = onFiltersClick,
            showIcon = true
        )
        
        // Sort chip
        FilterChip(
            text = getSortDisplayText(filters.sortOption),
            isSelected = filters.sortOption != SortOption.RECOMMENDED,
            onClick = onSortClick,
            showDropdown = true
        )
        
        // Price chip
        FilterChip(
            text = getPriceDisplayText(filters.priceRange.max),
            isSelected = filters.priceRange.max < 30000,
            onClick = onPriceClick,
            showDropdown = true
        )
        
        // Type chip
        FilterChip(
            text = getTypeDisplayText(filters.venueType),
            isSelected = filters.venueType != VenueType.EVERYONE,
            onClick = onTypeClick,
            showDropdown = true
        )
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    showIcon: Boolean = false,
    showDropdown: Boolean = false
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            if (showIcon && icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Filters",
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    )
                    
                    if (showDropdown) {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        },
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(18.dp),
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
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp
        )
    )
}

private fun getSortDisplayText(sortOption: SortOption): String {
    return when (sortOption) {
        SortOption.RECOMMENDED -> "Sort"
        SortOption.TOP_RATED -> "Top-rated"
        SortOption.NEAREST -> "Nearest"
    }
}

private fun getPriceDisplayText(maxPrice: Int): String {
    return if (maxPrice >= 30000) {
        "Price"
    } else {
        "₺${maxPrice / 100}"
    }
}

private fun getTypeDisplayText(venueType: VenueType): String {
    return when (venueType) {
        VenueType.EVERYONE -> "Type"
        VenueType.MALE_ONLY -> "Male only"
        VenueType.FEMALE_ONLY -> "Female only"
    }
}

@Preview
@Composable
private fun FilterChipBarPreview() {
    AynaAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Default state
            FilterChipBar(
                filters = ExploreFilters(),
                onFiltersClick = { },
                onSortClick = { },
                onPriceClick = { },
                onTypeClick = { }
            )
            
            // With filters applied
            FilterChipBar(
                filters = ExploreFilters(
                    sortOption = SortOption.TOP_RATED,
                    priceRange = com.techtactoe.ayna.domain.model.PriceRange(max = 15000),
                    venueType = VenueType.FEMALE_ONLY
                ),
                onFiltersClick = { },
                onSortClick = { },
                onPriceClick = { },
                onTypeClick = { }
            )
        }
    }
}
