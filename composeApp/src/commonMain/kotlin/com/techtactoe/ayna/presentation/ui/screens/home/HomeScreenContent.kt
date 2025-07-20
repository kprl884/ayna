package com.techtactoe.ayna.presentation.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.domain.model.ServiceCategory
import com.techtactoe.ayna.presentation.ui.components.BottomNavigation
import com.techtactoe.ayna.presentation.ui.components.SalonCard
import com.techtactoe.ayna.presentation.ui.components.ServiceCategoryCard
import com.techtactoe.ayna.presentation.ui.components.UserHeader
import com.techtactoe.ayna.presentation.viewmodel.HomeScreenState

@Composable
fun HomeScreenContent(
    state: HomeScreenState
) {
    val serviceCategories = listOf(
        ServiceCategory("1", "Hair & styling", "💇‍♀️"),
        ServiceCategory("2", "Nails", "💅"),
        ServiceCategory("3", "Eyebrows & eyelashes", "👁️"),
        ServiceCategory("4", "Massage", "💆‍♂️"),
        ServiceCategory("5", "Barbering", "✂️"),
        ServiceCategory("6", "Hair removal", "🪒"),
        ServiceCategory("7", "Facials & skincare", "🧴"),
        ServiceCategory("8", "Injectables & fillers", "💉"),
        ServiceCategory("9", "Body", "☀️"),
        ServiceCategory("10", "Tattoo & piercing", "🎨"),
        ServiceCategory("11", "Makeup", "💄"),
        ServiceCategory("12", "Medical & dental", "🦷")
    )

    Scaffold(
        topBar = {
            UserHeader(
                userName = "John",
                userInitials = "JS"
            )
        },
        bottomBar = {
            BottomNavigation()
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "❌",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Bir hata oluştu",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = state.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        // Greeting
                        Text(
                            text = "Hey, John",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                    
                    // Recommended salons section
                    if (state.salons.isNotEmpty()) {
                        item {
                            Column {
                                Text(
                                    text = "Recommended",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(state.salons) { salon ->
                                        SalonCard(salon = salon)
                                    }
                                }
                            }
                        }
                        
                        item {
                            Text(
                                text = "New to Fresha",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                        
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.salons.take(2)) { salon ->
                                    SalonCard(salon = salon)
                                }
                            }
                        }
                    }
                    
                    // Service categories section
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(800.dp) // Fixed height to work inside LazyColumn
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            userScrollEnabled = false // Disable grid scrolling since we're in LazyColumn
                        ) {
                            items(serviceCategories) { category ->
                                ServiceCategoryCard(category = category)
                            }
                        }
                    }
                    
                    // Bottom padding
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}
