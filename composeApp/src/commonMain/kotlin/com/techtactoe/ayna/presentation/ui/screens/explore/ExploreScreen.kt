package com.techtactoe.ayna.presentation.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.common.designsystem.ErrorContent
import com.techtactoe.ayna.common.designsystem.LoadingContent
import com.techtactoe.ayna.common.designsystem.button.PrimaryButton
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.BottomSheetType
import com.techtactoe.ayna.domain.model.ExploreError
import com.techtactoe.ayna.domain.model.Venue
import com.techtactoe.ayna.presentation.ui.screens.explore.components.ExploreSearchBar
import com.techtactoe.ayna.common.designsystem.chip.FilterChipBarRefactored
import com.techtactoe.ayna.common.designsystem.chip.FilterChipBarViewState
import com.techtactoe.ayna.presentation.ui.screens.explore.components.VenueCardRefactored
import com.techtactoe.ayna.presentation.ui.screens.explore.components.VenueCardViewState
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = ExploreViewModel(),
    onNavigateToVenueDetail: (String) -> Unit = {},
    onNavigateToMap: () -> Unit = {},
    onNavigateToAdvancedSearch: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.navigationState.navigateToVenueDetail) {
        uiState.navigationState.navigateToVenueDetail?.let { venueId ->
            onNavigateToVenueDetail(venueId)
            viewModel.onEvent(ExploreContract.UiEvent.OnNavigationHandled(ExploreContract.NavigationReset.VENUE_DETAIL))
        }
    }

    LaunchedEffect(uiState.navigationState.navigateToMap) {
        if (uiState.navigationState.navigateToMap) {
            onNavigateToMap()
            viewModel.onEvent(ExploreContract.UiEvent.OnNavigationHandled(ExploreContract.NavigationReset.MAP))
        }
    }

    LaunchedEffect(uiState.navigationState.navigateToAdvancedSearch) {
        if (uiState.navigationState.navigateToAdvancedSearch) {
            onNavigateToAdvancedSearch()
            viewModel.onEvent(ExploreContract.UiEvent.OnNavigationHandled(ExploreContract.NavigationReset.ADVANCED_SEARCH))
        }
    }

    LaunchedEffect(uiState.navigationState.snackbarMessage) {
        uiState.navigationState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(ExploreContract.UiEvent.OnSnackbarDismissed)
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExploreTopBarRefactored(
                filterState = uiState.filterState,
                scrollBehavior = scrollBehavior,
                onSearchBarClick = { viewModel.onEvent(ExploreContract.UiEvent.OnNavigateToAdvancedSearch) },
                onMapClick = { viewModel.onEvent(ExploreContract.UiEvent.OnNavigateToMap) },
                onFiltersClick = {
                    viewModel.onEvent(
                        ExploreContract.UiEvent.OnShowBottomSheet(BottomSheetType.Filters)
                    )
                },
                onSortClick = {
                    viewModel.onEvent(
                        ExploreContract.UiEvent.OnShowBottomSheet(BottomSheetType.Sort)
                    )
                },
                onPriceClick = {
                    viewModel.onEvent(
                        ExploreContract.UiEvent.OnShowBottomSheet(BottomSheetType.Price)
                    )
                },
                onTypeClick = {
                    viewModel.onEvent(
                        ExploreContract.UiEvent.OnShowBottomSheet(BottomSheetType.VenueType)
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        ExploreContentRefactored(
            contentState = uiState.contentState,
            onVenueClick = { venue ->
                viewModel.onEvent(ExploreContract.UiEvent.OnVenueClicked(venue.id))
            },
            onVenueBookmark = { venue ->
                viewModel.onEvent(ExploreContract.UiEvent.OnVenueBookmarked(venue.id))
            },
            onSeeMoreClick = { venue ->
                viewModel.onEvent(ExploreContract.UiEvent.OnVenueClicked(venue.id))
            },
            onRefresh = { viewModel.onEvent(ExploreContract.UiEvent.OnRefreshVenues) },
            onLoadMore = { viewModel.onEvent(ExploreContract.UiEvent.OnLoadMoreVenues) },
            onRetry = { viewModel.onEvent(ExploreContract.UiEvent.OnRetryLoadVenues) },
            onClearFilters = { viewModel.onEvent(ExploreContract.UiEvent.OnClearAllFilters) },
            modifier = Modifier.padding(paddingValues)
        )
    }

    // Bottom sheets - will be implemented in next phase
    when (uiState.filterState.bottomSheetType) {
        BottomSheetType.Sort -> {
            // TODO: Implement SortBottomSheetRefactored
        }

        BottomSheetType.Price -> {
            // TODO: Implement PriceBottomSheetRefactored
        }

        BottomSheetType.VenueType -> {
            // TODO: Implement VenueTypeBottomSheetRefactored
        }

        BottomSheetType.Filters -> {
            // TODO: Implement FiltersBottomSheetRefactored
        }

        BottomSheetType.None -> {
            // No bottom sheet shown
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreTopBarRefactored(
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

@Composable
private fun ExploreContentRefactored(
    contentState: com.techtactoe.ayna.domain.model.ExploreContentState,
    onVenueClick: (Venue) -> Unit,
    onVenueBookmark: (Venue) -> Unit,
    onSeeMoreClick: (Venue) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        contentState.isLoading && contentState.venues.isEmpty() -> {
            LoadingContent()
        }

        contentState.error != null -> {
            ErrorContentRefactored(
                error = contentState.error,
                onRetry = onRetry,
                onClearFilters = onClearFilters,
                modifier = modifier
            )
        }

        contentState.venues.isEmpty() -> {
            EmptyContentRefactored(
                message = "We didn't find a match",
                subMessage = "Try a new search",
                onClearFilters = onClearFilters,
                modifier = modifier
            )
        }

        else -> {
            SuccessContentRefactored(
                venues = contentState.venues,
                isRefreshing = contentState.isRefreshing,
                hasMorePages = contentState.hasMorePages,
                onVenueClick = onVenueClick,
                onVenueBookmark = onVenueBookmark,
                onSeeMoreClick = onSeeMoreClick,
                onRefresh = onRefresh,
                onLoadMore = onLoadMore,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun SuccessContentRefactored(
    venues: List<Venue>,
    isRefreshing: Boolean,
    hasMorePages: Boolean,
    onVenueClick: (Venue) -> Unit,
    onVenueBookmark: (Venue) -> Unit,
    onSeeMoreClick: (Venue) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= venues.size - 3 &&
                    hasMorePages &&
                    !isRefreshing
                ) {
                    onLoadMore()
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
        // Venue count header with design system typography
        if (venues.isNotEmpty()) {
            item(key = "venue_count_header") {
                Text(
                    text = "${venues.size} venues nearby",
                    style = AynaTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Venue cards with proper keys for performance
        items(
            items = venues,
            key = { venue -> venue.id }
        ) { venue ->
            VenueCardRefactored(
                viewState = VenueCardViewState(venue = venue),
                onVenueClick = { onVenueClick(venue) },
                onSeeMoreClick = { onSeeMoreClick(venue) },
                onBookmarkClick = { onVenueBookmark(venue) }
            )
        }

        // Loading more indicator with design system colors
        if (hasMorePages && venues.isNotEmpty()) {
            item(key = "loading_more_indicator") {
                Box(
                    modifier = Modifier
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
        }
    }
}

@Composable
private fun EmptyContentRefactored(
    message: String,
    subMessage: String,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xxlarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(Spacing.large))

        Text(
            text = message,
            style = AynaTypography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        Text(
            text = subMessage,
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.large))

        PrimaryButton(
            onClick = onClearFilters,
            text = "Clear search",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ErrorContentRefactored(
    error: ExploreError,
    onRetry: () -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (_, message, primaryAction, secondaryAction) = when (error) {
        is ExploreError.NetworkError -> ErrorInfo(
            title = "Connection Problem",
            message = "Please check your internet connection and try again.",
            primaryAction = "Retry" to onRetry,
            secondaryAction = null
        )

        is ExploreError.LocationPermissionDenied -> ErrorInfo(
            title = "Location Permission Needed",
            message = "Please enable location permission to find venues near you.",
            primaryAction = "Clear Filters" to onClearFilters,
            secondaryAction = null
        )

        is ExploreError.NoDataError -> ErrorInfo(
            title = "No Data Available",
            message = "Unable to load venue data at the moment.",
            primaryAction = "Retry" to onRetry,
            secondaryAction = "Clear Filters" to onClearFilters
        )

        is ExploreError.UnknownError -> ErrorInfo(
            title = "Something went wrong",
            message = error.message,
            primaryAction = "Retry" to onRetry,
            secondaryAction = "Clear Filters" to onClearFilters
        )
    }

    ErrorContent(
        message = message,
        onRetry = primaryAction.second,
        onClearError = secondaryAction?.second ?: onClearFilters
    )
}

/**
 * Data class to hold error information for better error handling
 */
private data class ErrorInfo(
    val title: String,
    val message: String,
    val primaryAction: Pair<String, () -> Unit>,
    val secondaryAction: Pair<String, () -> Unit>? = null
)

/**
 * Helper function to calculate active filters count
 * Following Single Responsibility Principle
 */
private fun calculateActiveFiltersCount(filters: com.techtactoe.ayna.domain.model.ExploreFiltersUiModel): Int {
    var count = 0

    if (filters.searchQuery.isNotBlank()) count++
    if (filters.selectedCity.isNotBlank()) count++
    if (filters.sortOption != com.techtactoe.ayna.domain.model.SortOption.RECOMMENDED) count++
    if (filters.priceRange.max < 30000) count++
    if (filters.venueType != com.techtactoe.ayna.domain.model.VenueType.EVERYONE) count++

    return count
}

// ExploreViewModelEnhanced is imported from the separate file

@Preview
@Composable
private fun ExploreScreenRefactoredPreview() {
    MaterialTheme {
        ExploreScreen()
    }
}

@Preview
@Composable
private fun EmptyContentRefactoredPreview() {
    MaterialTheme {
        EmptyContentRefactored(
            message = "We didn't find a match",
            subMessage = "Try a new search",
            onClearFilters = {}
        )
    }
}
