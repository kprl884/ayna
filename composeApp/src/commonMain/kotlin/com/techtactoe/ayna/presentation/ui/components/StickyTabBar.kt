package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.common.designsystem.component.icon.IconInCircle
import com.techtactoe.ayna.common.designsystem.component.icon.IconWithImageVector
import com.techtactoe.ayna.common.designsystem.theme.AnimationDuration
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.theme.StringResources
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import com.techtactoe.ayna.presentation.ui.model.SalonDetailTab
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class StickyTabBarViewState(
    val salonName: String,
    val selectedTab: SalonDetailTab,
    val isFavorite: Boolean = false
)

@Composable
fun StickyTabBar(
    viewState: StickyTabBarViewState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onTabClick: (SalonDetailTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(Spacing.medium)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconWithImageVector(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = StringResources.back_button_description,
                    modifier = Modifier
                        .clickable { onBackClick() }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = viewState.salonName,
                    style = AynaTypography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                IconInCircle(
                    onClick = { onFavoriteClick() },
                    resource = rememberVectorPainter(image = Icons.Outlined.Favorite),
                    shadowElevation = 0.dp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.medium)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(Spacing.large)
            ) {
                SalonDetailTab.entries.forEach { tab ->
                    TabItem(
                        tab = tab,
                        isSelected = viewState.selectedTab == tab,
                        onClick = { onTabClick(tab) }
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
        }

    }
}

@Composable
private fun TabItem(
    tab: SalonDetailTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = AnimationDuration.fast)
    )

    val indicatorAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = AnimationDuration.fast)
    )

    Box(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = Spacing.medium)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
        ) {
            Text(
                text = when (tab) {
                    SalonDetailTab.PHOTOS -> "Photos"
                    SalonDetailTab.SERVICES -> StringResources.services_text
                    SalonDetailTab.TEAM -> "Team"
                    SalonDetailTab.REVIEWS -> StringResources.reviews_text
                    SalonDetailTab.BUY -> "Buy"
                    SalonDetailTab.ABOUT -> StringResources.about_text
                },
                style = if (isSelected) AynaTypography.labelLarge else AynaTypography.labelMedium,
                color = textColor,
                maxLines = 1
            )

            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(2.dp)
                    .alpha(indicatorAlpha)
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
            )
        }
    }
}

@Preview
@Composable
fun StickyTabBarPreview() {
    val mockViewState = StickyTabBarViewState(
        salonName = "Hair Etc. Studio",
        selectedTab = SalonDetailTab.SERVICES,
        isFavorite = false
    )

    MaterialTheme {
        StickyTabBar(
            viewState = mockViewState,
            onTabClick = {},
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}
