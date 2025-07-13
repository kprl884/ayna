package org.style.customer.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import compose.icons.FeatherIcons
import compose.icons.feathericons.Codepen
import org.style.customer.data.mock.MockData
import org.style.customer.ui.components.common.CategoryCard
import org.style.customer.ui.components.common.SalonCard
import org.style.customer.ui.components.common.getCategoryColor
import org.style.customer.ui.components.common.getCategoryIcon

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
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = systemPadding.calculateTopPadding(), // Adjust for status bar
                        start = systemPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = systemPadding.calculateEndPadding(LayoutDirection.Ltr)
                    ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Merhaba 👋",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Bugün nasıl hissediyorsun?",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }

                        Icon(
                            imageVector = FeatherIcons.Codepen,
                            contentDescription = "Profile",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Favorites Section
                item {
                    Text(
                        text = "Favoriler",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
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
                    Text(
                        text = "Son Görüntülenenler",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
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
                    Text(
                        text = "Kategoriler",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
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