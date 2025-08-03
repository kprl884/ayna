package com.techtactoe.ayna.presentation.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.theme.StringResources
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import com.techtactoe.ayna.presentation.ui.screens.map.components.FilterBottomSheet
import com.techtactoe.ayna.presentation.ui.screens.map.components.FilterBottomSheetViewState
import com.techtactoe.ayna.presentation.ui.screens.map.components.SalonMapCard
import com.techtactoe.ayna.presentation.ui.screens.map.components.SalonMapCardViewState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapScreen(
    onBackClick: () -> Unit,
    onSalonClick: (String) -> Unit,
    viewModel: MapViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is MapContract.UiEffect.NavigateBack -> onBackClick()
                is MapContract.UiEffect.NavigateToSalonDetail -> onSalonClick(effect.salonId)
            }
        }
    }

    MapContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun MapContent(
    uiState: MapContract.UiState,
    onEvent: (MapContract.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Map View (placeholder for now)
        MapViewPlaceholder(
            pins = uiState.salonPins,
            center = uiState.mapCenter,
            onPinClick = { salonId ->
                onEvent(MapContract.UiEvent.OnSalonPinClick(salonId))
            }
        )

        // Header
        MapHeader(
            locationName = uiState.locationName,
            onBackClick = { onEvent(MapContract.UiEvent.OnBackClick) },
            onFilterClick = { onEvent(MapContract.UiEvent.OnFilterClick) },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Salon Cards Horizontal List (at bottom)
        uiState.selectedSalon?.let { salon ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Spacing.xlarge)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
                    modifier = Modifier.padding(horizontal = Spacing.medium)
                ) {
                    items(listOf(salon)) { salonCard ->
                        SalonMapCard(
                            viewState = SalonMapCardViewState(
                                salon = salonCard,
                                isVisible = true,
                                isFavorite = false
                            ),
                            onSalonClick = {
                                onEvent(MapContract.UiEvent.OnSalonCardClick(salonCard.id))
                            },
                            onDismiss = {
                                onEvent(MapContract.UiEvent.OnSalonCardDismiss)
                            }
                        )
                    }
                }
            }
        }

        // Filter Bottom Sheet
        FilterBottomSheet(
            viewState = FilterBottomSheetViewState(
                isVisible = uiState.isFilterVisible,
                filterState = uiState.filterState
            ),
            onFilterChange = { filterState ->
                onEvent(MapContract.UiEvent.OnFilterChange(filterState))
            },
            onApply = {
                onEvent(MapContract.UiEvent.OnApplyFilter)
            },
            onDismiss = {
                onEvent(MapContract.UiEvent.OnFilterDismiss)
            }
        )
    }
}

@Composable
private fun MapHeader(
    locationName: String,
    onBackClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = AynaShapes.medium
            )
            .statusBarsPadding()
            .padding(Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        // Top row with back button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = StringResources.back_button_description,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Map View",
                style = AynaTypography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Location search bar and filter button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Location indicator
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = AynaShapes.medium
                    )
                    .padding(
                        horizontal = Spacing.medium,
                        vertical = Spacing.small
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = locationName,
                    style = AynaTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.size(Spacing.small))

            // Filter button
            IconButton(
                onClick = onFilterClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color.Transparent,
                        shape = AynaShapes.medium
                    )
                    .clickable { onFilterClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun MapViewPlaceholder(
    pins: List<com.techtactoe.ayna.domain.model.SalonMapPin>,
    center: com.techtactoe.ayna.domain.model.LatLng,
    onPinClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // This is a placeholder for the actual map implementation
    // In production, you would use Google Maps for Android and MapKit for iOS
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            Text(
                text = "Map View Placeholder",
                style = AynaTypography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Center: ${center.latitude}, ${center.longitude}",
                style = AynaTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "${pins.size} salon pins",
                style = AynaTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Mock pins for demonstration
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                items(pins) { pin ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (pin.isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                },
                                shape = AynaShapes.large
                            )
                            .clickable { onPinClick(pin.salonId) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pin.salonId,
                            style = AynaTypography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    MaterialTheme {
        MapContent(
            uiState = MapContract.UiState(),
            onEvent = {}
        )
    }
}
