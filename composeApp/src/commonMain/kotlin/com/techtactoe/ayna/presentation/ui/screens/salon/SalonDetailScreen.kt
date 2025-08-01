package com.techtactoe.ayna.presentation.ui.screens.salon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.domain.model.SalonDetail
import com.techtactoe.ayna.presentation.theme.AynaColors
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

@Composable
fun SalonDetailScreen(
    salonDetail: SalonDetail,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onBookNowClick: () -> Unit,
    onServiceBookClick: (serviceId: String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    var selectedTab by remember { mutableStateOf(SalonDetailTab.SERVICES) }

    val showStickyTabBar by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 100
        }
    }

    Scaffold(
        containerColor = AynaColors.White,
        bottomBar = {
            FloatingBookingBar(
                serviceCount = salonDetail.services.size,
                onBookNowClick = onBookNowClick
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
                        onBackClick = onBackClick,
                        onShareClick = onShareClick,
                        onFavoriteClick = onFavoriteClick
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
                        onServiceBookClick = onServiceBookClick
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

                // Bottom padding for the floating bar
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // Sticky tab bar
            if (showStickyTabBar) {
                StickyTabBar(
                    selectedTab = selectedTab,
                    onTabClick = { tab ->
                        selectedTab = tab
                        // TODO: Scroll to the corresponding section
                    },
                    modifier = Modifier.fillMaxWidth(),
                    salonName = salonDetail.name,
                    onBackClick = onBackClick,
                    onShareClick = onShareClick,
                    onFavoriteClick = onFavoriteClick
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
