package com.techtactoe.ayna.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.techtactoe.ayna.presentation.ui.components.BottomNavItem
import com.techtactoe.ayna.presentation.ui.components.BottomNavigation
import com.techtactoe.ayna.presentation.ui.components.SalonCard
import com.techtactoe.ayna.presentation.ui.components.SectionHeader
import com.techtactoe.ayna.presentation.ui.components.UserHeader
import com.techtactoe.ayna.presentation.viewmodel.HomeScreenState
import com.techtactoe.ayna.theme.AynaColors

@Composable
fun HomeScreenContent(
    state: HomeScreenState
) {
    Scaffold(
        containerColor = AynaColors.White,
        bottomBar = {
            BottomNavigation(selectedItem = BottomNavItem.HOME)
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
                    CircularProgressIndicator(color = AynaColors.Purple)
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
                            color = AynaColors.SecondaryText
                        )
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AynaColors.White)
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header with greeting and avatar
                    UserHeader(
                        userName = "John",
                        userInitials = "JS"
                    )

                    // Recommended section
                    if (state.salons.isNotEmpty()) {
                        SectionHeader(title = "Recommended")

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.salons) { salon ->
                                SalonCard(salon = salon)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // New to Fresha section
                        SectionHeader(title = "New to Fresha")

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.salons.take(2)) { salon ->
                                SalonCard(salon = salon)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
