package com.techtactoe.ayna.presentation.ui.screens.salon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.domain.model.Review
import com.techtactoe.ayna.domain.model.TeamMember
import com.techtactoe.ayna.presentation.ui.components.AboutSection
import com.techtactoe.ayna.presentation.ui.components.AboutSectionViewState
import com.techtactoe.ayna.presentation.ui.components.BuySection
import com.techtactoe.ayna.presentation.ui.components.BuySectionViewState
import com.techtactoe.ayna.presentation.ui.components.FloatingBookingBar
import com.techtactoe.ayna.presentation.ui.components.FloatingBookingBarViewState
import com.techtactoe.ayna.presentation.ui.components.ReviewsSection
import com.techtactoe.ayna.presentation.ui.components.ReviewsSectionViewState
import com.techtactoe.ayna.presentation.ui.components.StickyTabBar
import com.techtactoe.ayna.presentation.ui.components.StickyTabBarViewState
import com.techtactoe.ayna.presentation.ui.components.TeamSection
import com.techtactoe.ayna.presentation.ui.components.TeamSectionViewState
import com.techtactoe.ayna.presentation.ui.screens.salon.components.ImageCarousel
import com.techtactoe.ayna.presentation.ui.screens.salon.components.SalonBasicInfo
import com.techtactoe.ayna.presentation.ui.screens.salon.components.ServicesSection
import com.techtactoe.ayna.util.LogLevel
import com.techtactoe.ayna.util.log
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SalonDetailContent(
    uiState: SalonDetailContract.UiState,
    onEvent: (SalonDetailContract.UiEvent) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val salonDetail = uiState.salonDetail

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            FloatingBookingBar(
                FloatingBookingBarViewState(
                    serviceCount = salonDetail?.services?.size ?: 0,
                    isVisible = uiState.showStickyTabBar,
                    isEnabled = true,
                ),
                onBookNowClick = {
                    onEvent(SalonDetailContract.UiEvent.OnBookNowClick)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = 0.dp,
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateRightPadding(LayoutDirection.Ltr)
                )
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    salonDetail?.images?.let { images ->
                        ImageCarousel(
                            images = images,
                            onBackClick = { onEvent(SalonDetailContract.UiEvent.OnBackClick) },
                            onShareClick = { onEvent(SalonDetailContract.UiEvent.OnShareClick) },
                            onFavoriteClick = { onEvent(SalonDetailContract.UiEvent.OnFavoriteClick) }
                        )
                    }
                }
                // Salon basic info
                item {
                    salonDetail?.let { salon ->
                        SalonBasicInfo(salonDetail = salonDetail)
                    }
                }
                // Photos section
                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }
                // Services section
                item {
                    salonDetail?.let { salon ->
                        ServicesSection(
                            services = salonDetail.services,
                            onServiceBookClick = { serviceId ->
                                onEvent(SalonDetailContract.UiEvent.OnServiceBookClick(serviceId))
                            }
                        )
                    }
                }
                // Team section
                item {
                    salonDetail?.let { salon ->
                        TeamSection(
                            TeamSectionViewState(
                                teamMembers = listOf(
                                    TeamMember(
                                        id = "1",
                                        name = "Marios",
                                        role = "Creative Director",
                                        imageUrl = null,
                                        rating = 5.0
                                    ),
                                    TeamMember(
                                        id = "2",
                                        name = "Ankit",
                                        role = "Support Team",
                                        imageUrl = null,
                                        rating = 5.0
                                    ),
                                    TeamMember(
                                        id = "3",
                                        name = "Fanouria",
                                        role = "Stylist",
                                        imageUrl = null,
                                        rating = 5.0
                                    )
                                )
                            )
                        )
                    }
                }
                // Reviews section
                item {
                    salonDetail?.let { salon ->
                        ReviewsSection(
                            ReviewsSectionViewState(
                                reviews = listOf(
                                    Review(
                                        id = "1",
                                        userName = "Monica T",
                                        userInitials = "MT",
                                        date = "Sat, Jul 19, 2025 at 10:28 PM",
                                        rating = 5,
                                        comment = "Only the best! Much appreciated 😊"
                                    ),
                                    Review(
                                        id = "2",
                                        userName = "Froso P",
                                        userInitials = "FP",
                                        date = "Sat, Jul 19, 2025 at 12:52 PM",
                                        rating = 5,
                                        comment = "Absolutely excellent service! The environment is welcoming, stylish, and relaxing. Special thanks to the team for their professionalism."
                                    )
                                ),
                                overallRating = 5.0,
                                reviewCount = 3645
                            )
                        )
                    }
                }
                // Buy section
                item {
                    salonDetail?.let { salon ->
                        BuySection(
                            BuySectionViewState(
                                buyOptions = salonDetail.buyOptions
                            )
                        )
                    }
                }
                // About section
                item {
                    salonDetail?.let { salon ->
                        AboutSection(
                            viewState = AboutSectionViewState(
                                description = salonDetail.about.description,
                                openingHours = salonDetail.openingHours,
                            )
                        )
                    }
                }
            }
            if (uiState.showStickyTabBar) {
                log(
                    LogLevel.DEBUG,
                    "alpstein",
                    "if (uiState.showStickyTabBar) {" + uiState.showStickyTabBar
                )

                StickyTabBar(
                    onTabClick = { tab ->
                        onEvent(SalonDetailContract.UiEvent.OnTabSelected(tab))
                        // TODO: Scroll to the corresponding section
                    },
                    modifier = Modifier.fillMaxWidth(),
                    onBackClick = { onEvent(SalonDetailContract.UiEvent.OnBackClick) },
                    onFavoriteClick = { onEvent(SalonDetailContract.UiEvent.OnFavoriteClick) },
                    viewState = StickyTabBarViewState(
                        salonName = salonDetail?.name ?: "",
                        selectedTab = uiState.selectedTab,
                    )
                )
            }
        }
    }
}

@Composable
@Preview
fun SalonDetailContentPreview() {
    MaterialTheme {
        SalonDetailContent(
            uiState = SalonDetailContract.UiState(),
            onEvent = {},
            scrollState = rememberLazyListState()
        )
    }
}