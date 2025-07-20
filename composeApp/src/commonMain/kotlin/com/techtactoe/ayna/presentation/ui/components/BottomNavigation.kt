package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.presentation.model.BottomNavItem
import com.techtactoe.ayna.presentation.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavigation(
    selectedItem: BottomNavItem = BottomNavItem.HOME,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        shadowElevation = 4.dp,
        color = AynaColors.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavIcon(
                    icon = "🏠",
                    isSelected = selectedItem == BottomNavItem.HOME,
                    onClick = { onItemClick(BottomNavItem.HOME) },
                )

                NavIcon(
                    icon = "🔍",
                    isSelected = selectedItem == BottomNavItem.SEARCH,
                    onClick = { onItemClick(BottomNavItem.SEARCH) },
                )

                NavIcon(
                    icon = "📅",
                    isSelected = selectedItem == BottomNavItem.CALENDAR,
                    onClick = { onItemClick(BottomNavItem.CALENDAR) },
                )
            }
        }
    }
}

@Composable
private fun NavIcon(
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (isSelected) AynaColors.Purple else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    radius = 20.dp,
                    color = if (isSelected) AynaColors.White else AynaColors.Purple
                ),
                role = Role.Tab,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 24.sp,
            color = if (isSelected) AynaColors.White else AynaColors.InactiveGray
        )
    }
}

@Preview
@Composable
fun BottomNavigationPreview() {
    MaterialTheme {
        BottomNavigation()
    }
}

@Preview
@Composable
fun BottomNavigationSearchSelectedPreview() {
    MaterialTheme {
        BottomNavigation(selectedItem = BottomNavItem.SEARCH)
    }
}

@Preview
@Composable
fun BottomNavigationCalendarSelectedPreview() {
    MaterialTheme {
        BottomNavigation(selectedItem = BottomNavItem.CALENDAR)
    }
}