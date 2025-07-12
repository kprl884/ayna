package org.style.customer.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.style.customer.ui.components.buttons.PrimaryButton
import org.style.customer.ui.components.cards.BusinessCard
import org.style.customer.ui.theme.StringResources

/**
 * Home Screen - Main landing page
 */
class HomeScreen : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = StringResources.welcome_message,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Search Section
            PrimaryButton(
                text = StringResources.search_placeholder,
                onClick = { /* TODO: Navigate to search */ }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Featured Businesses
            Text(
                text = "Öne Çıkan İşletmeler",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sample Business Cards
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleBusinesses) { business ->
                    BusinessCard(
                        name = business.name,
                        rating = business.rating,
                        reviewCount = business.reviewCount,
                        distance = business.distance,
                        onClick = { /* TODO: Navigate to business detail */ }
                    )
                }
            }
        }
    }
    
    private val sampleBusinesses = listOf(
        SampleBusiness("Güzellik Salonu A", 4.5f, 128, "0.5 km"),
        SampleBusiness("Kuaför B", 4.8f, 256, "1.2 km"),
        SampleBusiness("Spa Merkezi C", 4.3f, 89, "2.1 km")
    )
    
    private data class SampleBusiness(
        val name: String,
        val rating: Float,
        val reviewCount: Int,
        val distance: String
    )
} 