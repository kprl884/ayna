package org.style.customer.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.style.customer.data.mock.MockData
import org.style.customer.ui.components.common.CategoryCard
import org.style.customer.ui.components.common.SalonCard
import org.style.customer.ui.components.common.getCategoryColor
import org.style.customer.ui.components.common.getCategoryIcon
import org.style.customer.ui.designsystem.components.text.AppText
import org.style.customer.ui.designsystem.components.text.CategoryTitle

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val categories = listOf(
            "Saç",
            "Makyaj",
            "Cilt Bakımı",
            "Manikür",
            "Pedikür",
            "Masaj",
            "Epilasyon",
            "Kaş & Kirpik"
        )
        val systemPadding = WindowInsets.statusBars.asPaddingValues()
        Surface(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = systemPadding.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            AppText(
                                text = "Merhaba",
                                style = org.style.customer.ui.designsystem.foundation.typography.AppTextStyles.businessName,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                // Favorites Section
                item {
                    CategoryTitle(
                        text = "Favoriler",
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyRow(
                        modifier = Modifier.padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(MockData.favoriteSalons) { salon ->
                            SalonCard(
                                salon = salon,
                                onClick = {
                                    // TODO: Navigate to salon detail
                                }
                            )
                        }
                    }
                }

                // Recently Viewed
                item {
                    CategoryTitle(
                        text = "Son Görüntülenenler",
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(MockData.recentlyViewedSalons) { salon ->
                            SalonCard(
                                salon = salon,
                                onClick = {
                                    // TODO: Navigate to salon detail
                                }
                            )
                        }
                    }
                }

                // Categories
                item {
                    CategoryTitle(
                        modifier = Modifier.padding(top = 24.dp),
                        text = "Kategoriler",
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(200.dp)
                    ) {
                        items(categories) { category ->
                            CategoryCard(
                                title = category,
                                icon = getCategoryIcon(category),
                                backgroundColor = getCategoryColor(category),
                                onClick = {
                                    // TODO: Navigate to category screen
                                }
                            )
                        }
                    }
                }
            }
        }
    }
} 