package org.style.customer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import org.style.customer.data.mock.MockData
import org.style.customer.ui.components.FilterChip
import org.style.customer.ui.components.OpeningHoursRow
import org.style.customer.ui.components.RatingStars
import org.style.customer.ui.components.ReviewCard
import org.style.customer.ui.components.ServiceCard
import org.style.customer.ui.components.TeamMemberCard
import org.style.customer.ui.theme.AynaShapes

@OptIn(ExperimentalMaterial3Api::class)
class HairSalonBookingScreen() : Screen {
    @Composable
    override fun Content() {
        var selectedTab by remember { mutableStateOf(0) }
        val tabs = listOf("Photos", "Services", "Team", "Reviews", "About")
        var selectedCategory by remember { mutableStateOf(MockData.serviceCategories.first()) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Tania S Hair and He...",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* TODO: Back */ }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                null
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Share */ }) {
                            Icon(
                                Icons.Default.Share,
                                null
                            )
                        }
                        IconButton(onClick = { /* TODO: Favorite */ }) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("38 services available", color = Color.Gray)
                    Button(
                        onClick = { /* TODO: Book now */ },
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f)
                            .padding(start = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Book now", color = Color.White)
                    }
                }
            }
        ) { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = Color.Black
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(tab) }
                        )
                    }
                }
                when (selectedTab) {
                    0 -> PhotosTab()
                    1 -> ServicesTab(selectedCategory, onCategorySelect = { selectedCategory = it })
                    2 -> TeamTab()
                    3 -> ReviewsTab()
                    4 -> AboutTab()
                }
            }
        }
    }
}

@Composable
fun PhotosTab() {
    LazyRow(
        Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(MockData.photos) { photoUrl ->
            Box(
                Modifier
                    .width(320.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray, AynaShapes.large)
            ) {
                // TODO: Image loader
            }
        }
    }
}

@Composable
fun ServicesTab(selectedCategory: String, onCategorySelect: (String) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Row(Modifier.horizontalScroll(rememberScrollState())) {
            MockData.serviceCategories.forEach { category ->
                FilterChip(selected = selectedCategory == category, label = category) {
                    onCategorySelect(category)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        val filtered = MockData.services.filter { it.category == selectedCategory }
        filtered.forEach { service ->
            ServiceCard(service = service, onBook = { /* TODO: Book */ })
        }
    }
}

@Composable
fun TeamTab() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Team", style = MaterialTheme.typography.titleLarge)
        TextButton(onClick = { /* TODO: See all */ }) { Text("See all") }
    }
    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(MockData.team) { member ->
            TeamMemberCard(member)
        }
    }
}

@Composable
fun ReviewsTab() {
    Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("5.0", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Spacer(Modifier.width(8.dp))
            RatingStars(5.0f)
            Spacer(Modifier.width(8.dp))
            Text("(6)", color = Color.Gray)
        }
        Spacer(Modifier.height(16.dp))
        MockData.reviews.forEach { review ->
            ReviewCard(review)
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        }
    }
}

@Composable
fun AboutTab() {
    Column(Modifier.padding(16.dp)) {
        Text(
            "A charming, elegant, and serene space with limited seating, offering personalized hair services for all hair types and conditions. It's...",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        Text("Opening Hours", style = MaterialTheme.typography.titleMedium)
        MockData.openingHours.forEach { OpeningHoursRow(it) }
        Spacer(Modifier.height(16.dp))
        Text("Instant confirmation", color = Color.Black, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Κωνσταντίνου Σκόκου 2, Εμπορικό Κέντρο, Λευκωσία", color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.LightGray, AynaShapes.large),
            contentAlignment = Alignment.Center
        ) {
            Text("TODO: Map integration", color = Color.DarkGray)
        }
        Spacer(Modifier.height(16.dp))
        Text("Nearby Venues", style = MaterialTheme.typography.titleMedium)
        MockData.nearbyVenues.forEach {
            Text("${it.name} • ${it.rating} (${it.reviewCount})", color = Color.Gray)
        }
    }
} 