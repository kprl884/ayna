package com.techtactoe.ayna.presentation.ui.screens.salon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.common.designsystem.ErrorContent
import com.techtactoe.ayna.common.designsystem.LoadingContent

/**
 * Content for the SalonDetail screen, handling UI rendering based on state
 */
@Composable
fun SalonDetailScreenContent(
    uiState: SalonDetailContract.UiState,
    onEvent: (SalonDetailContract.UiEvent) -> Unit,
    scrollState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            LoadingContent()
        }

        uiState.errorMessage != null -> {
            ErrorContent(
                message = uiState.errorMessage,
                onRetry = { onEvent(SalonDetailContract.UiEvent.OnInitialize(uiState.salonId)) },
                onClearError = { onEvent(SalonDetailContract.UiEvent.OnClearError) }
            )
        }

        uiState.salonDetail != null -> {
            SalonDetailContent(
                uiState = uiState,
                onEvent = onEvent,
                scrollState = scrollState,
                modifier = modifier
            )
        }
    }
}