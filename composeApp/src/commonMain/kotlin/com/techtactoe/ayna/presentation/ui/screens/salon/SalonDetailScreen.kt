package com.techtactoe.ayna.presentation.ui.screens.salon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.techtactoe.ayna.presentation.theme.Spacing
import com.techtactoe.ayna.presentation.theme.brandPurple
import com.techtactoe.ayna.presentation.ui.components.AboutSection
import com.techtactoe.ayna.presentation.ui.components.BuySection
import com.techtactoe.ayna.presentation.ui.components.FloatingBookingBar
import com.techtactoe.ayna.presentation.ui.screens.salon.components.ImageCarousel
import com.techtactoe.ayna.presentation.ui.components.ReviewsSection
import com.techtactoe.ayna.presentation.ui.components.SalonDetailTab
import com.techtactoe.ayna.presentation.ui.screens.salon.components.ServicesSection
import com.techtactoe.ayna.presentation.ui.components.StickyTabBar
import com.techtactoe.ayna.presentation.ui.components.TeamSection
import com.techtactoe.ayna.presentation.ui.screens.salon.components.SalonBasicInfo
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen displaying detailed information about a salon
 * Following the golden standard MVVM pattern
 */
@Composable
fun SalonDetailScreen(
    uiState: SalonDetailContract.UiState,
    onEvent: (SalonDetailContract.UiEvent) -> Unit,
    salonId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Initialize with salon ID
    LaunchedEffect(salonId) {
        onEvent(SalonDetailContract.UiEvent.OnInitialize(salonId))
    }

    // Handle navigation effects
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            navController.navigateUp()
            onEvent(SalonDetailContract.UiEvent.OnNavigationHandled(SalonDetailContract.NavigationReset.BACK))
        }
    }

    LaunchedEffect(uiState.navigateToBooking) {
        if (uiState.navigateToBooking) {
            navController.navigate("select_time/${uiState.salonId}")
            onEvent(SalonDetailContract.UiEvent.OnNavigationHandled(SalonDetailContract.NavigationReset.BOOKING))
        }
    }

    LaunchedEffect(uiState.navigateToServiceBooking) {
        uiState.navigateToServiceBooking?.let { serviceId ->
            navController.navigate("select_time/${uiState.salonId}/$serviceId")
            onEvent(SalonDetailContract.UiEvent.OnNavigationHandled(SalonDetailContract.NavigationReset.SERVICE_BOOKING))
        }
    }

    LaunchedEffect(uiState.shareContent) {
        uiState.shareContent?.let { content ->
            // TODO: Implement actual sharing functionality
            onEvent(SalonDetailContract.UiEvent.OnNavigationHandled(SalonDetailContract.NavigationReset.SHARE))
        }
    }

    val scrollState = rememberLazyListState()

    // Monitor scroll state for sticky tab bar
    val showStickyTabBar by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 100
        }
    }

    LaunchedEffect(showStickyTabBar) {
        onEvent(SalonDetailContract.UiEvent.OnScrollStateChanged(showStickyTabBar))
    }

    when {
        uiState.isLoading -> {
            LoadingContent()
        }
        uiState.errorMessage != null -> {
            ErrorContent(
                message = uiState.errorMessage,
                onRetry = { onEvent(SalonDetailContract.UiEvent.OnInitialize(salonId)) },
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

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.brandPurple)
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onClearError: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.md)
        )

        androidx.compose.material3.Button(
            onClick = onRetry,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Try again",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun SalonDetailContent(
    uiState: SalonDetailContract.UiState,
    onEvent: (SalonDetailContract.UiEvent) -> Unit,
    scrollState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    val salonDetail = uiState.salonDetail!!

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            FloatingBookingBar(
                serviceCount = salonDetail.services.size,
                onBookNowClick = { onEvent(SalonDetailContract.UiEvent.OnBookNowClick) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    ImageCarousel(
                        images = salonDetail.images,
                        onBackClick = { onEvent(SalonDetailContract.UiEvent.OnBackClick) },
                        onShareClick = { onEvent(SalonDetailContract.UiEvent.OnShareClick) },
                        onFavoriteClick = { onEvent(SalonDetailContract.UiEvent.OnFavoriteClick) }
                    )
                }

                // Salon basic info
                item {
                    SalonBasicInfo(salonDetail = salonDetail)
                }

                // Photos section
                item {
                    // Placeholder for photos section (can reuse image carousel or extend it)
                    Spacer(modifier = Modifier.height(0.dp))
                }

                // Services section
                item {
                    ServicesSection(
                        services = salonDetail.services,
                        onServiceBookClick = { serviceId ->
                            onEvent(SalonDetailContract.UiEvent.OnServiceBookClick(serviceId))
                        }
                    )
                }

                // Team section
                item {
                    TeamSection(teamMembers = salonDetail.team)
                }

                // Reviews section
                item {
                    ReviewsSection(
                        reviews = salonDetail.reviews,
                        overallRating = salonDetail.rating,
                        reviewCount = salonDetail.reviewCount
                    )
                }

                // Buy section
                item {
                    BuySection(buyOptions = salonDetail.buyOptions)
                }

                // About section
                item {
                    AboutSection(
                        about = salonDetail.about,
                        openingHours = salonDetail.openingHours
                    )
                }

            }

            // Sticky tab bar
            if (uiState.showStickyTabBar) {
                StickyTabBar(
                    selectedTab = uiState.selectedTab,
                    onTabClick = { tab ->
                        onEvent(SalonDetailContract.UiEvent.OnTabSelected(tab))
                        // TODO: Scroll to the corresponding section
                    },
                    modifier = Modifier.fillMaxWidth(),
                    salonName = salonDetail.name,
                    onBackClick = { onEvent(SalonDetailContract.UiEvent.OnBackClick) },
                    onShareClick = { onEvent(SalonDetailContract.UiEvent.OnShareClick) },
                    onFavoriteClick = { onEvent(SalonDetailContract.UiEvent.OnFavoriteClick) }
                )
            }
        }
    }
}

@Preview
@Composable
fun SalonDetailScreenPreview() {
    // Mock data would go here
    MaterialTheme {
        // SalonDetailScreen(...)
    }
}
