package com.techtactoe.ayna.presentation.ui.screens.salon

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen displaying detailed information about a salon
 * Following the golden standard MVVM pattern
 */
@Composable
fun SalonDetailScreen(
    uiState: SalonDetailContract.UiState,
    onEvent: (SalonDetailContract.UiEvent) -> Unit,
    navigateUp: () -> Unit,
    navigateSelectTimeOnSalonDetailScreen: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    val showStickyTabBar by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 100
        }
    }

    if (showStickyTabBar != uiState.showStickyTabBar) {
        onEvent(SalonDetailContract.UiEvent.OnScrollStateChanged(showStickyTabBar))
    }

    SalonDetailScreenContent(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is SalonDetailContract.UiEvent.OnBackClick -> {
                    navigateUp()
                }

                is SalonDetailContract.UiEvent.OnBookNowClick -> {
                    uiState.salonDetail?.services?.firstOrNull()?.let { firstService ->
                        navigateSelectTimeOnSalonDetailScreen(
                            uiState.salonId,
                            firstService.id
                        )
                    }
                }

                is SalonDetailContract.UiEvent.OnServiceBookClick -> {
                    navigateSelectTimeOnSalonDetailScreen(
                        uiState.salonId, event.serviceId
                    )
                }

                is SalonDetailContract.UiEvent.OnShareClick -> {
                    uiState.salonDetail?.let { salon ->
                        "Check out ${salon.name} on Ayna! ${salon.about.description}"
                    } ?: "Check out this amazing salon on Ayna!"
                    // TODO: Implement actual sharing functionality with shareText
                }

                else -> {
                    onEvent(event)
                }
            }
        },
        scrollState = scrollState,
        modifier = modifier
    )
}

@Preview
@Composable
fun SalonDetailScreenPreview() {
    MaterialTheme {
        SalonDetailScreen(
            uiState = SalonDetailContract.UiState(),
            onEvent = {},
            navigateUp = {},
            navigateSelectTimeOnSalonDetailScreen = { _, _ -> },
            modifier = Modifier
        )
    }
}