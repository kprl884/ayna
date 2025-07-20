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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.SalonDetail
import com.techtactoe.ayna.domain.model.SalonStatus
import com.techtactoe.ayna.presentation.ui.components.AboutSection
import com.techtactoe.ayna.presentation.ui.components.BuySection
import com.techtactoe.ayna.presentation.ui.components.FloatingBookingBar
import com.techtactoe.ayna.presentation.ui.components.ImageCarousel
import com.techtactoe.ayna.presentation.ui.components.ReviewsSection
import com.techtactoe.ayna.presentation.ui.components.SalonDetailTab
import com.techtactoe.ayna.presentation.ui.components.ServicesSection
import com.techtactoe.ayna.presentation.ui.components.StickyTabBar
import com.techtactoe.ayna.presentation.ui.components.TeamSection
import com.techtactoe.ayna.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SalonDetailScreen(
    salonDetail: SalonDetail,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onBookNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    var selectedTab by remember { mutableStateOf(SalonDetailTab.SERVICES) }
    
    // Determine if we should show the sticky tab bar based on scroll position
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
                // Image carousel header
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
                    ServicesSection(services = salonDetail.services)
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
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SalonBasicInfo(
    salonDetail: SalonDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Salon name
        Text(
            text = salonDetail.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AynaColors.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Rating and reviews
        Text(
            text = "${salonDetail.rating} ⭐⭐⭐⭐⭐ (${salonDetail.reviewCount})",
            fontSize = 16.sp,
            color = AynaColors.Black
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Address
        Text(
            text = salonDetail.address,
            fontSize = 14.sp,
            color = AynaColors.SecondaryText
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Status
        val statusText = when (salonDetail.status) {
            SalonStatus.OPEN -> "Open now"
            SalonStatus.CLOSED -> "Closed"
            SalonStatus.OPENS_LATER -> "Closed - opens on Tuesday at 9:00 AM"
        }
        
        Text(
            text = statusText,
            fontSize = 14.sp,
            color = AynaColors.SecondaryText
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Featured tag
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Featured",
                fontSize = 14.sp,
                color = AynaColors.Purple,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
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
