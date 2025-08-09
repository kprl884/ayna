package com.techtactoe.ayna.presentation.ui.screens.explore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.Venue

@Composable
fun SuccessContentRefactored(
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