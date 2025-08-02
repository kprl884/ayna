package com.techtactoe.ayna.presentation.ui.screens.salon

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen displaying detailed information about a salon.
 * This Composable is now "dumb" - it only displays state and sends events.
 * It has NO KNOWLEDGE of navigation.
 */
@Composable
fun SalonDetailScreen(
    uiState: SalonDetailContract.UiState,
    onEvent: (SalonDetailContract.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    val showStickyTabBar by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 ||
                    (scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset > 300)
        }
    }

    // This effect now correctly sends the event to the ViewModel via the onEvent lambda
    LaunchedEffect(showStickyTabBar) {
        onEvent(SalonDetailContract.UiEvent.OnScrollStateChanged(showStickyTabBar))
    }

    // We pass the onEvent lambda directly down to the content
    SalonDetailScreenContent(
        uiState = uiState,
        onEvent = onEvent, // Pass the original onEvent
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
            modifier = Modifier
        )
    }
}