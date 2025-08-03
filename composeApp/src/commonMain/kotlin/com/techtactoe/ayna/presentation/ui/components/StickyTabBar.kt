package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.designsystem.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class SalonDetailTab {
    PHOTOS, SERVICES, TEAM, REVIEWS, BUY, ABOUT
}

@Composable
fun StickyTabBar(
    salonName: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    selectedTab: SalonDetailTab,
    onTabClick: (SalonDetailTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AynaColors.White)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onBackClick() },
                    shape = CircleShape,
                    color = AynaColors.White,
                    shadowElevation = 2.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "←",
                            fontSize = 18.sp,
                            color = AynaColors.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(salonName)
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onShareClick() },
                    shape = CircleShape,
                    color = AynaColors.White,
                    shadowElevation = 2.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "↗",
                            fontSize = 18.sp,
                            color = AynaColors.Black
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onFavoriteClick() },
                    shape = CircleShape,
                    color = AynaColors.White,
                    shadowElevation = 2.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♡",
                            fontSize = 18.sp,
                            color = AynaColors.Black
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SalonDetailTab.entries.forEach { tab ->
                    TabItem(
                        tab = tab,
                        isSelected = selectedTab == tab,
                        onClick = { onTabClick(tab) }
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = AynaColors.BorderGray
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
    Box(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 16.dp)
    ) {
        Column {
            Text(
                text = when (tab) {
                    SalonDetailTab.PHOTOS -> "Photos"
                    SalonDetailTab.SERVICES -> "Services"
                    SalonDetailTab.TEAM -> "Team"
                    SalonDetailTab.REVIEWS -> "Reviews"
                    SalonDetailTab.BUY -> "Buy"
                    SalonDetailTab.ABOUT -> "About"
                },
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) AynaColors.Black else AynaColors.SecondaryText
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(2.dp)
                        .background(AynaColors.Black)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview
@Composable
fun StickyTabBarPreview() {
    MaterialTheme {
        StickyTabBar(
            selectedTab = SalonDetailTab.SERVICES,
            onTabClick = {},
            salonName = "Salon Name",
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {}
        )
    }
}
