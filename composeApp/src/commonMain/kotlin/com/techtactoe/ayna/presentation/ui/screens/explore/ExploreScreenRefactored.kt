package com.techtactoe.ayna.presentation.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import com.techtactoe.ayna.designsystem.button.PrimaryButton
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.BottomSheetType
import com.techtactoe.ayna.domain.model.ExploreError
import com.techtactoe.ayna.domain.model.VenueUiModel
import com.techtactoe.ayna.presentation.ui.screens.explore.components.ExploreSearchBar
import com.techtactoe.ayna.presentation.ui.screens.explore.components.FilterChipBarRefactored
import com.techtactoe.ayna.presentation.ui.screens.explore.components.FilterChipBarViewState
import com.techtactoe.ayna.presentation.ui.screens.explore.components.VenueCardRefactored
import com.techtactoe.ayna.presentation.ui.screens.explore.components.VenueCardViewState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Refactored ExploreScreen following MVVM best practices and design system integration
 * 
 * Key improvements:
 * - Complete design system integration (colors, typography, spacing)
 * - Atomic component architecture
 * - Performance optimizations with @Stable annotations
 * - Proper error handling
 * - SOLID principles compliance
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreenRefactored(
    onNavigateToVenueDetail: (String) -> Unit = {},
    onNavigateToMap: () -> Unit = {},
    onNavigateToAdvancedSearch: () -> Unit = {},
    viewModel: ExploreViewModelRefactored = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle side effects
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ExploreContractEnhanced.UiEffect.NavigateToVenueDetail -> {
                    onNavigateToVenueDetail(effect.venueId)
                }
                is ExploreContractEnhanced.UiEffect.NavigateToMap -> {
                    onNavigateToMap()
                }
                is ExploreContractEnhanced.UiEffect.NavigateToAdvancedSearch -> {
                    onNavigateToAdvancedSearch()
                }
                is ExploreContractEnhanced.UiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is ExploreContractEnhanced.UiEffect.RequestLocationPermission -> {
                    // Handle location permission request
                    // Platform-specific implementation needed
                }
                is ExploreContractEnhanced.UiEffect.ShareVenue -> {
                    // Handle venue sharing
                    // Platform-specific implementation needed
                }
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExploreTopBarRefactored(
                filterState = uiState.filterState,
                scrollBehavior = scrollBehavior,
                onEvent = viewModel::onEvent
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        ExploreContentRefactored(
            contentState = uiState.contentState,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(paddingValues)
        )
    }

    // Handle bottom sheets
    when (uiState.filterState.bottomSheetType) {
        is BottomSheetType.Sort -> {
            // TODO: Implement SortBottomSheet
        }
        is BottomSheetType.Price -> {
            // TODO: Implement PriceBottomSheet
        }
        is BottomSheetType.VenueType -> {
            // TODO: Implement VenueTypeBottomSheet
        }
        is BottomSheetType.Filters -> {
            // TODO: Implement FiltersBottomSheet
        }
        is BottomSheetType.None -> {
            // No bottom sheet shown
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreTopBarRefactored(
    filterState: com.techtactoe.ayna.domain.model.ExploreFilterState,
    scrollBehavior: androidx.compose.material3.TopAppBarScrollBehavior,
    onEvent: (ExploreContractEnhanced.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        // Top spacing
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(Spacing.small))

        // Search bar
        ExploreSearchBar(
            searchQuery = filterState.filters.searchQuery,
            selectedCity = filterState.filters.selectedCity,
            onSearchBarClick = { 
                onEvent(ExploreContractEnhanced.UiEvent.OnNavigateToAdvancedSearch) 
            },
            onMapClick = { 
                onEvent(ExploreContractEnhanced.UiEvent.OnNavigateToMap) 
            }
        )

        // Filter chips
        FilterChipBarRefactored(
            viewState = FilterChipBarViewState(
                filters = filterState.filters,
                activeFiltersCount = calculateActiveFiltersCount(filterState.filters)
            ),
            onFiltersClick = {
                onEvent(
                    ExploreContractEnhanced.UiEvent.OnShowBottomSheet(
                        BottomSheetType.Filters
                    )
                )
            },
            onSortClick = {
                onEvent(
                    ExploreContractEnhanced.UiEvent.OnShowBottomSheet(
                        BottomSheetType.Sort
                    )
                )
            },
            onPriceClick = {
                onEvent(
                    ExploreContractEnhanced.UiEvent.OnShowBottomSheet(
                        BottomSheetType.Price
                    )
                )
            },
            onTypeClick = {
                onEvent(
                    ExploreContractEnhanced.UiEvent.OnShowBottomSheet(
                        BottomSheetType.VenueType
                    )
                )
            }
        )

        // Bottom spacing
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(Spacing.small))
    }
}

@Composable
private fun ExploreContentRefactored(
    contentState: com.techtactoe.ayna.domain.model.ExploreContentState,
    onEvent: (ExploreContractEnhanced.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        contentState.isLoading && contentState.venues.isEmpty() -> {
            LoadingContentRefactored(modifier = modifier)
        }

        contentState.error != null -> {
            ErrorContentRefactored(
                error = contentState.error,
                onRetry = { onEvent(ExploreContractEnhanced.UiEvent.OnRetryLoadVenues) },
                modifier = modifier
            )
        }

        contentState.venues.isEmpty() -> {
            EmptyContentRefactored(
                onClearSearch = { onEvent(ExploreContractEnhanced.UiEvent.OnClearAllFilters) },
                modifier = modifier
            )
        }

        else -> {
            SuccessContentRefactored(
                venues = contentState.venues,
                isRefreshing = contentState.isRefreshing,
                hasMorePages = contentState.hasMorePages,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LoadingContentRefactored(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun SuccessContentRefactored(
    venues: List<VenueUiModel>,
    isRefreshing: Boolean,
    hasMorePages: Boolean,
    onEvent: (ExploreContractEnhanced.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Handle pagination
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && 
                    lastVisibleIndex >= venues.size - 3 && 
                    hasMorePages && 
                    !isRefreshing) {
                    onEvent(ExploreContractEnhanced.UiEvent.OnLoadMoreVenues)
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = Spacing.medium, 
            vertical = Spacing.small
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        // Venue count header
        if (venues.isNotEmpty()) {
            item(key = "venue_count_header") {
                VenueCountHeader(count = venues.size)
            }
        }

        // Venue cards
        items(
            items = venues,
            key = { venue -> venue.id }
        ) { venue ->
            VenueCardRefactored(
                viewState = VenueCardViewState(venue = venue),
                onVenueClick = { 
                    onEvent(ExploreContractEnhanced.UiEvent.OnVenueClicked(venue.id)) 
                },
                onSeeMoreClick = { 
                    onEvent(ExploreContractEnhanced.UiEvent.OnVenueClicked(venue.id)) 
                },
                onBookmarkClick = { 
                    onEvent(ExploreContractEnhanced.UiEvent.OnVenueBookmarked(venue.id)) 
                }
            )
        }

        // Loading more indicator
        if (hasMorePages && venues.isNotEmpty()) {
            item(key = "loading_more") {
                LoadingMoreIndicator()
            }
        }
    }
}

@Composable
private fun VenueCountHeader(
    count: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "$count venues nearby",
        style = AynaTypography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun LoadingMoreIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.dp
        )
    }
}

@Composable
private fun ErrorContentRefactored(
    error: ExploreError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xxxlarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = getErrorTitle(error),
            style = AynaTypography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(Spacing.small))

        Text(
            text = getErrorMessage(error),
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(Spacing.large))

        PrimaryButton(
            text = "Try again",
            onClick = onRetry
        )
    }
}

@Composable
private fun EmptyContentRefactored(
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xxxlarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(Spacing.large))

        Text(
            text = "We didn't find a match",
            style = AynaTypography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(Spacing.small))

        Text(
            text = "Try a new search",
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(Spacing.large))

        PrimaryButton(
            text = "Clear search",
            onClick = onClearSearch
        )
    }
}

// Helper functions
private fun calculateActiveFiltersCount(filters: com.techtactoe.ayna.domain.model.ExploreFiltersUiModel): Int {
    var count = 0
    if (filters.searchQuery.isNotEmpty()) count++
    if (filters.sortOption != com.techtactoe.ayna.domain.model.SortOption.RECOMMENDED) count++
    if (filters.priceRange.max < 30000) count++
    if (filters.venueType != com.techtactoe.ayna.domain.model.VenueType.EVERYONE) count++
    return count
}

private fun getErrorTitle(error: ExploreError): String {
    return when (error) {
        is ExploreError.NetworkError -> "Connection Problem"
        is ExploreError.LocationPermissionDenied -> "Location Access Needed"
        is ExploreError.NoDataError -> "No Data Available"
        is ExploreError.UnknownError -> "Something Went Wrong"
    }
}

private fun getErrorMessage(error: ExploreError): String {
    return when (error) {
        is ExploreError.NetworkError -> "Please check your internet connection and try again."
        is ExploreError.LocationPermissionDenied -> "Allow location access to find venues near you."
        is ExploreError.NoDataError -> "We couldn't load the venue data. Please try again later."
        is ExploreError.UnknownError -> error.message
    }
}

@Preview
@Composable
private fun ExploreScreenRefactoredPreview() {
    MaterialTheme {
        ExploreContentRefactored(
            contentState = com.techtactoe.ayna.domain.model.ExploreContentState(
                venues = emptyList()
            ),
            onEvent = {}
        )
    }
}
