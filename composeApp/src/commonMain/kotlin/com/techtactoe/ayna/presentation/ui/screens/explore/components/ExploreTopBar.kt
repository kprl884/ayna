package com.techtactoe.ayna.presentation.ui.screens.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.common.designsystem.component.chip.FilterChipBarRefactored
import com.techtactoe.ayna.common.designsystem.component.chip.FilterChipBarViewState
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.presentation.ui.screens.explore.helper.calculateActiveFiltersCount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreTopBar(
    filterState: com.techtactoe.ayna.domain.model.ExploreFilterState,
    scrollBehavior: androidx.compose.material3.TopAppBarScrollBehavior,
    onSearchBarClick: () -> Unit,
    onMapClick: () -> Unit,
    onFiltersClick: () -> Unit,
    onSortClick: () -> Unit,
    onPriceClick: () -> Unit,
    onTypeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Spacing.medium)
    ) {
        Spacer(modifier = Modifier.height(Spacing.small))

        // Search bar - reusing existing component with design system values
        ExploreSearchBar(
            searchQuery = filterState.filters.searchQuery,
            selectedCity = filterState.filters.selectedCity,
            onSearchBarClick = onSearchBarClick,
            onMapClick = onMapClick
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Filter chips with proper design system integration
        FilterChipBarRefactored(
            viewState = FilterChipBarViewState(
                filters = filterState.filters,
                activeFiltersCount = calculateActiveFiltersCount(filterState.filters)
            ),
            onFiltersClick = onFiltersClick,
            onSortClick = onSortClick,
            onPriceClick = onPriceClick,
            onTypeClick = onTypeClick
        )

        Spacer(modifier = Modifier.height(Spacing.small))
    }
}