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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.ExploreFilters
import com.techtactoe.ayna.domain.model.PriceRange
import com.techtactoe.ayna.domain.model.Venue
import com.techtactoe.ayna.presentation.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.ui.screens.explore.components.ExploreSearchBar
import com.techtactoe.ayna.presentation.ui.screens.explore.components.FilterChipBar
import com.techtactoe.ayna.presentation.ui.screens.explore.components.FiltersBottomSheet
import com.techtactoe.ayna.presentation.ui.screens.explore.components.PriceBottomSheet
import com.techtactoe.ayna.presentation.ui.screens.explore.components.SortBottomSheet
import com.techtactoe.ayna.presentation.ui.screens.explore.components.VenueCard
import com.techtactoe.ayna.presentation.ui.screens.explore.components.VenueTypeBottomSheet
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Main Explore screen with venue discovery and filtering
 * Features sticky header with search bar and filter chips
 */
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

    // Handle navigation events
    LaunchedEffect(uiState.navigateToVenueDetail) {
        uiState.navigateToVenueDetail?.let { venueId ->
            onNavigateToVenueDetail(venueId)
            viewModel.onEvent(ExploreContract.UiEvent.OnNavigationHandled(ExploreContract.NavigationReset.VENUE_DETAIL))
        }
    }

    LaunchedEffect(uiState.navigateToMap) {
        if (uiState.navigateToMap) {
            onNavigateToMap()
            viewModel.onEvent(ExploreContract.UiEvent.OnNavigationHandled(ExploreContract.NavigationReset.MAP))
        }
    }

    LaunchedEffect(uiState.navigateToAdvancedSearch) {
        if (uiState.navigateToAdvancedSearch) {
            onNavigateToAdvancedSearch()
            viewModel.onEvent(ExploreContract.UiEvent.OnNavigationHandled(ExploreContract.NavigationReset.ADVANCED_SEARCH))
        }
    }

    // Handle snackbar messages
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(ExploreContract.UiEvent.OnSnackbarDismissed)
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExploreTopBar(
                filters = uiState.filters,
                scrollBehavior = scrollBehavior,
                onSearchBarClick = { viewModel.onEvent(ExploreContract.UiEvent.OnNavigateToAdvancedSearch) },
                onMapClick = { viewModel.onEvent(ExploreContract.UiEvent.OnNavigateToMap) },
                onFiltersClick = {
                    viewModel.onEvent(
                        ExploreContract.UiEvent.OnShowBottomSheet(
                            BottomSheetType.Filters
                        )
                    )
                },
                onSortClick = {
                    viewModel.onEvent(
                        ExploreContract.UiEvent.OnShowBottomSheet(
                            BottomSheetType.Sort
                        )
                    )
                },
                onPriceClick = {
                    viewModel.onEvent(
                        ExploreContract.UiEvent.OnShowBottomSheet(
                            BottomSheetType.Price
                        )
                    )
                },
                onTypeClick = {
                    viewModel.onEvent(
                        ExploreContract.UiEvent.OnShowBottomSheet(
                            BottomSheetType.VenueType
                        )
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        ExploreContent(
            venues = uiState.venues,
            isLoading = uiState.isLoading,
            isRefreshing = uiState.isRefreshing,
            hasMorePages = uiState.hasMorePages,
            errorMessage = uiState.errorMessage,
            onVenueClick = { venue -> viewModel.onEvent(ExploreContract.UiEvent.OnVenueClicked(venue.id)) },
            onSeeMoreClick = { venue ->
                viewModel.onEvent(
                    ExploreContract.UiEvent.OnVenueClicked(
                        venue.id
                    )
                )
            },
            onRefresh = { viewModel.onEvent(ExploreContract.UiEvent.OnRefreshVenues) },
            onLoadMore = { viewModel.onEvent(ExploreContract.UiEvent.OnLoadMoreVenues) },
            onClearSearch = { viewModel.onEvent(ExploreContract.UiEvent.OnClearFilters) },
            modifier = Modifier.padding(paddingValues)
        )
    }

    // Bottom sheets
    when (uiState.currentBottomSheet) {
        is BottomSheetType.Sort -> {
            SortBottomSheet(
                currentSort = uiState.filters.sortOption,
                onSortSelected = { sortOption ->
                    val newFilters = uiState.tempFilters.copy(sortOption = sortOption)
                    viewModel.onEvent(ExploreContract.UiEvent.OnUpdateTempFilters(newFilters))
                    viewModel.onEvent(ExploreContract.UiEvent.OnApplyTempFilters)
                },
                onDismiss = { viewModel.onEvent(ExploreContract.UiEvent.OnHideBottomSheet) }
            )
        }

        is BottomSheetType.Price -> {
            PriceBottomSheet(
                currentPriceRange = uiState.filters.priceRange,
                onPriceRangeChanged = { priceRange ->
                    val newFilters = uiState.tempFilters.copy(priceRange = priceRange)
                    viewModel.onEvent(ExploreContract.UiEvent.OnUpdateTempFilters(newFilters))
                },
                onClear = {
                    val newFilters = uiState.tempFilters.copy(priceRange = PriceRange())
                    viewModel.onEvent(ExploreContract.UiEvent.OnUpdateTempFilters(newFilters))
                },
                onApply = { viewModel.onEvent(ExploreContract.UiEvent.OnApplyTempFilters) },
                onDismiss = { viewModel.onEvent(ExploreContract.UiEvent.OnHideBottomSheet) }
            )
        }

        is BottomSheetType.VenueType -> {
            VenueTypeBottomSheet(
                currentType = uiState.filters.venueType,
                onTypeSelected = { venueType ->
                    val newFilters = uiState.tempFilters.copy(venueType = venueType)
                    viewModel.onEvent(ExploreContract.UiEvent.OnUpdateTempFilters(newFilters))
                    viewModel.onEvent(ExploreContract.UiEvent.OnApplyTempFilters)
                },
                onDismiss = { viewModel.onEvent(ExploreContract.UiEvent.OnHideBottomSheet) }
            )
        }

        is BottomSheetType.Filters -> {
            FiltersBottomSheet(
                currentFilters = uiState.tempFilters,
                onFiltersChanged = { filters ->
                    viewModel.onEvent(ExploreContract.UiEvent.OnUpdateTempFilters(filters))
                },
                onClearAll = {
                    viewModel.onEvent(ExploreContract.UiEvent.OnUpdateTempFilters(ExploreFilters()))
                },
                onApply = { viewModel.onEvent(ExploreContract.UiEvent.OnApplyTempFilters) },
                onDismiss = { viewModel.onEvent(ExploreContract.UiEvent.OnHideBottomSheet) }
            )
        }

        is BottomSheetType.None -> {
            // No bottom sheet shown
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreTopBar(
    filters: ExploreFilters,
    @Suppress("UNUSED_PARAMETER") scrollBehavior: TopAppBarScrollBehavior,
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
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Search bar
        ExploreSearchBar(
            searchQuery = filters.searchQuery,
            selectedCity = filters.selectedCity,
            onSearchBarClick = onSearchBarClick,
            onMapClick = onMapClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter chips
        FilterChipBar(
            filters = filters,
            onFiltersClick = onFiltersClick,
            onSortClick = onSortClick,
            onPriceClick = onPriceClick,
            onTypeClick = onTypeClick
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ExploreContent(
    venues: List<Venue>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    hasMorePages: Boolean,
    errorMessage: String?,
    onVenueClick: (Venue) -> Unit,
    onSeeMoreClick: (Venue) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        isLoading && venues.isEmpty() -> {
            LoadingContent(modifier = modifier)
        }

        errorMessage != null -> {
            ErrorContent(
                message = errorMessage,
                onRetry = onRefresh,
                modifier = modifier
            )
        }

        venues.isEmpty() -> {
            EmptyContent(
                message = "We didn't find a match",
                subMessage = "Try a new search",
                onClearSearch = onClearSearch,
                modifier = modifier
            )
        }

        else -> {
            SuccessContent(
                venues = venues,
                isRefreshing = isRefreshing,
                hasMorePages = hasMorePages,
                onVenueClick = onVenueClick,
                onSeeMoreClick = onSeeMoreClick,
                onRefresh = onRefresh,
                onLoadMore = onLoadMore,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF7B61FF)
        )
    }
}

@Composable
private fun SuccessContent(
    venues: List<Venue>,
    isRefreshing: Boolean,
    hasMorePages: Boolean,
    onVenueClick: (Venue) -> Unit,
    onSeeMoreClick: (Venue) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= venues.size - 3 && hasMorePages) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Venue count header
        if (venues.isNotEmpty()) {
            item {
                Text(
                    text = "${venues.size} venues nearby",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Venue cards
        items(venues) { venue ->
            VenueCard(
                venue = venue,
                onVenueClick = { onVenueClick(venue) },
                onSeeMoreClick = { onSeeMoreClick(venue) }
            )
        }

        // Loading more indicator
        if (hasMorePages && venues.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF7B61FF),
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF000000),
                contentColor = Color.White
            )
        ) {
            Text("Try again")
        }
    }
}

@Composable
private fun EmptyContent(
    message: String,
    subMessage: String,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF7B61FF)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onClearSearch,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF000000),
                contentColor = Color.White
            )
        ) {
            Text("Clear search")
        }
    }
}

@Preview
@Composable
private fun ExploreScreenPreview() {
    AynaAppTheme {
        ExploreContent(
            venues = sampleVenues(),
            isLoading = false,
            isRefreshing = false,
            hasMorePages = true,
            errorMessage = null,
            onVenueClick = { },
            onSeeMoreClick = { },
            onRefresh = { },
            onLoadMore = { },
            onClearSearch = { }
        )
    }
}

@Preview()
@Composable
private fun ExploreScreenDarkPreview() {
    AynaAppTheme(darkTheme = true) {
        ExploreContent(
            venues = sampleVenues(),
            isLoading = false,
            isRefreshing = false,
            hasMorePages = true,
            errorMessage = null,
            onVenueClick = { },
            onSeeMoreClick = { },
            onRefresh = { },
            onLoadMore = { },
            onClearSearch = { }
        )
    }
}

@Preview()
@Composable
private fun ExploreContentLoadingPreview() {
    AynaAppTheme {
        ExploreContent(
            venues = emptyList(),
            isLoading = true,
            isRefreshing = false,
            hasMorePages = false,
            errorMessage = null,
            onVenueClick = { },
            onSeeMoreClick = { },
            onRefresh = { },
            onLoadMore = { },
            onClearSearch = { }
        )
    }
}
