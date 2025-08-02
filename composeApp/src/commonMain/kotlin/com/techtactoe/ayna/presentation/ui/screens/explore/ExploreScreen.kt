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
import kotlinx.coroutines.flow.collectLatest
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
    val screenState by viewModel.screenState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle events
    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ExploreEvent.NavigateToVenueDetail -> onNavigateToVenueDetail(event.venueId)
                is ExploreEvent.NavigateToMap -> onNavigateToMap()
                is ExploreEvent.NavigateToAdvancedSearch -> onNavigateToAdvancedSearch()
                is ExploreEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExploreTopBar(
                uiState = screenState.uiState,
                scrollBehavior = scrollBehavior,
                onSearchBarClick = { viewModel.navigateToAdvancedSearch() },
                onMapClick = { viewModel.navigateToMap() },
                onFiltersClick = { viewModel.showBottomSheet(BottomSheetType.Filters) },
                onSortClick = { viewModel.showBottomSheet(BottomSheetType.Sort) },
                onPriceClick = { viewModel.showBottomSheet(BottomSheetType.Price) },
                onTypeClick = { viewModel.showBottomSheet(BottomSheetType.VenueType) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        ExploreContent(
            uiState = screenState.uiState,
            onVenueClick = { venue -> viewModel.navigateToVenueDetail(venue.id) },
            onSeeMoreClick = { venue -> viewModel.navigateToVenueDetail(venue.id) },
            onRefresh = { viewModel.handleIntent(ExploreIntent.RefreshVenues) },
            onLoadMore = { viewModel.handleIntent(ExploreIntent.LoadMoreVenues) },
            onClearSearch = { viewModel.handleIntent(ExploreIntent.ClearFilters) },
            modifier = Modifier.padding(paddingValues)
        )
    }

    // Bottom sheets
    when (screenState.currentBottomSheet) {
        is BottomSheetType.Sort -> {
            val currentFilters = getCurrentFilters(screenState.uiState)
            SortBottomSheet(
                currentSort = currentFilters.sortOption,
                onSortSelected = { sortOption ->
                    viewModel.updateTempFilters(
                        screenState.tempFilters.copy(sortOption = sortOption)
                    )
                    viewModel.applyTempFilters()
                },
                onDismiss = { viewModel.hideBottomSheet() }
            )
        }

        is BottomSheetType.Price -> {
            val currentFilters = getCurrentFilters(screenState.uiState)
            PriceBottomSheet(
                currentPriceRange = currentFilters.priceRange,
                onPriceRangeChanged = { priceRange ->
                    viewModel.updateTempFilters(
                        screenState.tempFilters.copy(priceRange = priceRange)
                    )
                },
                onClear = {
                    viewModel.updateTempFilters(
                        screenState.tempFilters.copy(priceRange = PriceRange())
                    )
                },
                onApply = { viewModel.applyTempFilters() },
                onDismiss = { viewModel.hideBottomSheet() }
            )
        }

        is BottomSheetType.VenueType -> {
            val currentFilters = getCurrentFilters(screenState.uiState)
            VenueTypeBottomSheet(
                currentType = currentFilters.venueType,
                onTypeSelected = { venueType ->
                    viewModel.updateTempFilters(
                        screenState.tempFilters.copy(venueType = venueType)
                    )
                    viewModel.applyTempFilters()
                },
                onDismiss = { viewModel.hideBottomSheet() }
            )
        }

        is BottomSheetType.Filters -> {
            getCurrentFilters(screenState.uiState)
            FiltersBottomSheet(
                currentFilters = screenState.tempFilters,
                onFiltersChanged = { filters ->
                    viewModel.updateTempFilters(filters)
                },
                onClearAll = {
                    viewModel.updateTempFilters(ExploreFilters())
                },
                onApply = { viewModel.applyTempFilters() },
                onDismiss = { viewModel.hideBottomSheet() }
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
    uiState: ExploreUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    onSearchBarClick: () -> Unit,
    onMapClick: () -> Unit,
    onFiltersClick: () -> Unit,
    onSortClick: () -> Unit,
    onPriceClick: () -> Unit,
    onTypeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentFilters = getCurrentFilters(uiState)

    Column(
        modifier = modifier
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Search bar
        ExploreSearchBar(
            searchQuery = currentFilters.searchQuery,
            selectedCity = currentFilters.selectedCity,
            onSearchBarClick = onSearchBarClick,
            onMapClick = onMapClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter chips
        FilterChipBar(
            filters = currentFilters,
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
    uiState: ExploreUiState,
    onVenueClick: (Venue) -> Unit,
    onSeeMoreClick: (Venue) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ExploreUiState.Loading -> {
            LoadingContent(modifier = modifier)
        }

        is ExploreUiState.Success -> {
            if (uiState.isLoading) {
                LoadingContent(modifier = modifier)
            } else {
                SuccessContent(
                    venues = uiState.venues,
                    isRefreshing = uiState.isRefreshing,
                    hasMorePages = uiState.hasMorePages,
                    onVenueClick = onVenueClick,
                    onSeeMoreClick = onSeeMoreClick,
                    onRefresh = onRefresh,
                    onLoadMore = onLoadMore,
                    modifier = modifier
                )
            }
        }

        is ExploreUiState.Error -> {
            ErrorContent(
                message = uiState.message,
                onRetry = onRefresh,
                modifier = modifier
            )
        }

        is ExploreUiState.Empty -> {
            EmptyContent(
                message = uiState.message,
                subMessage = uiState.subMessage,
                onClearSearch = onClearSearch,
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

private fun getCurrentFilters(uiState: ExploreUiState): ExploreFilters {
    return when (uiState) {
        is ExploreUiState.Success -> uiState.filters
        is ExploreUiState.Error -> uiState.filters
        is ExploreUiState.Empty -> uiState.filters
        else -> ExploreFilters()
    }
}

@Preview
@Composable
private fun ExploreScreenPreview() {
    AynaAppTheme {
        // Create a mock ViewModel with Sort bottom sheet active
        val mockViewModel = ExploreViewModel().apply {
            showBottomSheet(BottomSheetType.Sort)
        }

        ExploreScreen(
            viewModel = mockViewModel,
            onNavigateToVenueDetail = { },
            onNavigateToMap = { },
            onNavigateToAdvancedSearch = { }
        )
    }
}

@Preview()
@Composable
private fun ExploreScreenDarkPreview() {
    AynaAppTheme(darkTheme = true) {
        val sampleUiState = ExploreUiState.Success(
            isLoading = false,
            venues = sampleVenues(),
            isRefreshing = false,
            hasMorePages = true,
            filters = ExploreFilters(),
            isLocationPermissionGranted = false
        )
        ExploreContent(
            uiState = sampleUiState,
            onVenueClick = { },
            onSeeMoreClick = { },
            onRefresh = { },
            onLoadMore = { },
            onClearSearch = { }
        )
    }
}
