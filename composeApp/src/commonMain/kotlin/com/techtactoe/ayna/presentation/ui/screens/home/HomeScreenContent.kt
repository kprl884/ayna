package com.techtactoe.ayna.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.theme.StringResources
import com.techtactoe.ayna.domain.model.NotificationUiState
import com.techtactoe.ayna.presentation.ui.components.SalonCard
import com.techtactoe.ayna.presentation.ui.components.SalonCardViewState
import com.techtactoe.ayna.presentation.ui.components.SectionHeader
import com.techtactoe.ayna.presentation.ui.components.UserHeader
import com.techtactoe.ayna.presentation.ui.components.UserHeaderViewState

@Composable
fun HomeScreenContent(
    uiState: HomeContract.UiState,
    onEvent: (HomeContract.UiEvent) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val topPadding = this.maxHeight / 15
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
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
                                text = uiState.errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(topPadding))

                        UserHeader(
                            UserHeaderViewState(
                                userName = uiState.userName,

                                notificationState = NotificationUiState(
                                    hasUnreadNotifications = true,
                                    unreadCount = 3
                                )
                            ),
                            onNotificationClick = {
                                onEvent(HomeContract.UiEvent.OnNavigateToNotifications)
                            }
                        )

                        Spacer(modifier = Modifier.height(Spacing.large))

                        if (uiState.salons.isNotEmpty()) {
                            SectionHeader(title = "Recommended")

                            Spacer(modifier = Modifier.height(Spacing.large))

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.salons) { salon ->
                                    SalonCard(
                                        SalonCardViewState(
                                            id = "1",
                                            name = "Barbershop Dolapdere",
                                            address = "Dolapdere Mahallesi, 34384 Istanbul",
                                            rating = 5.0,
                                            reviewCount = 69,
                                            imageUrl = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=800",
                                            tags = listOf(
                                                StringResources.barber_text,
                                                StringResources.popular_text
                                            ),
                                            isNew = true
                                        ),
                                        onSalonClick = { salonId ->
                                            onEvent(HomeContract.UiEvent.OnSalonClick(salonId))
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(Spacing.large))

                            SectionHeader(title = "New to Fresha")

                            Spacer(modifier = Modifier.height(Spacing.large))

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.salons.take(2)) { salon ->
                                    SalonCard(
                                        viewState = SalonCardViewState(
                                            id = salon.id,
                                            name = salon.name,
                                            address = salon.address,
                                            rating = salon.rating,
                                            reviewCount = salon.reviewCount,
                                            imageUrl = salon.imageUrl,
                                            tags = salon.tags
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(Spacing.xlarge))
                        }
                    }
                }
            }
        }
    }
}
