package com.techtactoe.ayna.presentation.ui.screens.explore.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.common.designsystem.component.content.EmptyContent
import com.techtactoe.ayna.common.designsystem.component.content.ErrorContent
import com.techtactoe.ayna.common.designsystem.component.content.LoadingContent
import com.techtactoe.ayna.domain.model.Venue

@Composable
fun ExploreContent(
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
            ErrorContent(
                error = contentState.error,
                onRetry = onRetry,
                onClearFilters = onClearFilters,
                modifier = modifier
            )
        }

        contentState.venues.isEmpty() -> {
            EmptyContent(
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