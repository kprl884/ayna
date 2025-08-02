package com.techtactoe.ayna.presentation.ui.screens.salon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.presentation.ui.components.AboutSection
import com.techtactoe.ayna.presentation.ui.components.BuySection
import com.techtactoe.ayna.presentation.ui.components.FloatingBookingBar
import com.techtactoe.ayna.presentation.ui.components.ReviewsSection
import com.techtactoe.ayna.presentation.ui.components.StickyTabBar
import com.techtactoe.ayna.presentation.ui.components.TeamSection
import com.techtactoe.ayna.presentation.ui.screens.salon.components.ImageCarousel
import com.techtactoe.ayna.presentation.ui.screens.salon.components.SalonBasicInfo
import com.techtactoe.ayna.presentation.ui.screens.salon.components.ServicesSection
import com.techtactoe.ayna.util.LogLevel
import com.techtactoe.ayna.util.log

@Composable
fun SalonDetailContent(
    uiState: SalonDetailContract.UiState,
    onEvent: (SalonDetailContract.UiEvent) -> Unit,
    scrollState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    val salonDetail = uiState.salonDetail

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            FloatingBookingBar(
                serviceCount = salonDetail?.services?.size ?: 0,
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
                        TeamSection(teamMembers = salonDetail.team)
                    }
                }
                // Reviews section
                item {
                    salonDetail?.let { salon ->
                        ReviewsSection(
                            reviews = salonDetail.reviews,
                            overallRating = salonDetail.rating,
                            reviewCount = salonDetail.reviewCount
                        )
                    }
                }
                // Buy section
                item {
                    salonDetail?.let { salon ->
                        BuySection(buyOptions = salonDetail.buyOptions)
                    }
                }
                // About section
                item {
                    salonDetail?.let { salon ->
                        AboutSection(
                            about = salonDetail.about,
                            openingHours = salonDetail.openingHours
                        )
                    }
                }
            }
            // Sticky tab bar
            if (uiState.showStickyTabBar) {
                log(LogLevel.DEBUG, "alpstein", "if (uiState.showStickyTabBar) {" +  uiState.showStickyTabBar)

                StickyTabBar(
                    selectedTab = uiState.selectedTab,
                    onTabClick = { tab ->
                        onEvent(SalonDetailContract.UiEvent.OnTabSelected(tab))
                        // TODO: Scroll to the corresponding section
                    },
                    modifier = Modifier.fillMaxWidth(),
                    salonName = salonDetail?.name.orEmpty(),
                    onBackClick = { onEvent(SalonDetailContract.UiEvent.OnBackClick) },
                    onShareClick = { onEvent(SalonDetailContract.UiEvent.OnShareClick) },
                    onFavoriteClick = { onEvent(SalonDetailContract.UiEvent.OnFavoriteClick) }
                )
            }
        }
    }
}