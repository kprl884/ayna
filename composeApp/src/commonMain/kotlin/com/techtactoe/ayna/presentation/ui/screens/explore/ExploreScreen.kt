package com.techtactoe.ayna.presentation.ui.screens.explore

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.techtactoe.ayna.domain.model.BottomSheetType
import com.techtactoe.ayna.presentation.ui.screens.explore.components.ExploreContent
import com.techtactoe.ayna.presentation.ui.screens.explore.components.ExploreTopBar
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = com.techtactoe.ayna.di.SupabaseDataModule.createExploreViewModel(),
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
            ExploreTopBar(
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
        ExploreContent(
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

@Preview
@Composable
private fun ExploreScreenRefactoredPreview() {
    MaterialTheme {
        ExploreScreen()
    }
}
