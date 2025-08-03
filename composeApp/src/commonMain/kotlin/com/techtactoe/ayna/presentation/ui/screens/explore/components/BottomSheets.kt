package com.techtactoe.ayna.presentation.ui.screens.explore.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.domain.model.*
import com.techtactoe.ayna.designsystem.theme.AynaAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Sort options bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSort: SortOption,
    onSortSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        // FIXED: Renamed 'windowInsets' to 'contentWindowInsets' and provided it as a lambda.
        contentWindowInsets = { WindowInsets(0.dp) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sort by",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sort options
            SortOption.entries.forEach { option ->
                RadioButtonItem(
                    text = getSortOptionDisplayName(option),
                    selected = currentSort == option,
                    onClick = { onSortSelected(option) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Price range bottom sheet with slider
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceBottomSheet(
    currentPriceRange: PriceRange,
    onPriceRangeChanged: (PriceRange) -> Unit,
    onClear: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tempMaxPrice by remember { mutableFloatStateOf(currentPriceRange.max.toFloat()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        // FIXED: Renamed 'windowInsets' to 'contentWindowInsets' and provided it as a lambda.
        contentWindowInsets = { WindowInsets(0.dp) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Price",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Price label
            Text(
                text = "Maximum price",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Price value
            Text(
                text = "₺${(tempMaxPrice / 100).toInt()}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color(0xFF7B61FF),
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Price slider
            Slider(
                value = tempMaxPrice,
                onValueChange = {
                    tempMaxPrice = it
                    onPriceRangeChanged(currentPriceRange.copy(max = it.toInt()))
                },
                valueRange = 0f..30000f,
                steps = 299, // 100 TRY steps
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF7B61FF),
                    activeTrackColor = Color(0xFF7B61FF),
                    inactiveTrackColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Clear")
                }

                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF000000),
                        contentColor = Color.White
                    )
                ) {
                    Text("Apply")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Venue type bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueTypeBottomSheet(
    currentType: VenueType,
    onTypeSelected: (VenueType) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        // FIXED: Renamed 'windowInsets' to 'contentWindowInsets' and provided it as a lambda.
        contentWindowInsets = { WindowInsets(0.dp) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Venue type",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Venue type options
            VenueType.entries.forEach { type ->
                RadioButtonItem(
                    text = getVenueTypeDisplayName(type),
                    selected = currentType == type,
                    onClick = { onTypeSelected(type) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Combined filters bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    currentFilters: ExploreFilters,
    onFiltersChanged: (ExploreFilters) -> Unit,
    onClearAll: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tempFilters by remember { mutableStateOf(currentFilters) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        // FIXED: Renamed 'windowInsets' to 'contentWindowInsets' and provided it as a lambda.
        contentWindowInsets = { WindowInsets(0.dp) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sort by section
            Text(
                text = "Sort by",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            SortOption.entries.forEach { option ->
                RadioButtonItem(
                    text = getSortOptionDisplayName(option),
                    selected = tempFilters.sortOption == option,
                    onClick = {
                        tempFilters = tempFilters.copy(sortOption = option)
                        onFiltersChanged(tempFilters)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Maximum price section
            Text(
                text = "Maximum price",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "₺${tempFilters.priceRange.max / 100}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color(0xFF7B61FF),
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Slider(
                value = tempFilters.priceRange.max.toFloat(),
                onValueChange = {
                    tempFilters = tempFilters.copy(
                        priceRange = tempFilters.priceRange.copy(max = it.toInt())
                    )
                    onFiltersChanged(tempFilters)
                },
                valueRange = 0f..30000f,
                steps = 299,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF7B61FF),
                    activeTrackColor = Color(0xFF7B61FF),
                    inactiveTrackColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Venue type section
            Text(
                text = "Venue type",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Venue type chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VenueType.entries.forEach { type ->
                    FilterChip(
                        selected = tempFilters.venueType == type,
                        onClick = {
                            tempFilters = tempFilters.copy(venueType = type)
                            onFiltersChanged(tempFilters)
                        },
                        label = {
                            Text(
                                text = getVenueTypeDisplayName(type),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF7B61FF),
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = tempFilters.venueType == type,
                            borderColor = MaterialTheme.colorScheme.outline,
                            selectedBorderColor = Color(0xFF7B61FF)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClearAll,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Clear all")
                }

                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF000000),
                        contentColor = Color.White
                    )
                ) {
                    Text("Apply")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RadioButtonItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF7B61FF),
                unselectedColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

private fun getSortOptionDisplayName(option: SortOption): String {
    return when (option) {
        SortOption.RECOMMENDED -> "Recommended"
        SortOption.TOP_RATED -> "Top-rated"
        SortOption.NEAREST -> "Nearest"
        SortOption.PRICE_LOW_TO_HIGH ->"PRICE_LOW_TO_HIGH"
        SortOption.PRICE_HIGH_TO_LOW -> "PRICE_HIGH_TO_LOW"
    }
}

private fun getVenueTypeDisplayName(type: VenueType): String {
    return when (type) {
        VenueType.EVERYONE -> "Everyone"
        VenueType.MALE_ONLY -> "Male only"
        VenueType.FEMALE_ONLY -> "Female only"
    }
}

@Preview
@Composable
private fun BottomSheetsPreview() {
    AynaAppTheme {
        // This would typically show individual bottom sheets in separate previews
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Bottom sheet previews - use individual components")
        }
    }
}