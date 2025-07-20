package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class BottomNavItem {
    HOME, SEARCH, CALENDAR
}

@Composable
fun BottomNavigation(
    selectedItem: BottomNavItem = BottomNavItem.HOME,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = AynaColors.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home icon (selected)
            NavIcon(
                icon = "🏠",
                isSelected = selectedItem == BottomNavItem.HOME
            )
            
            // Search icon
            NavIcon(
                icon = "🔍",
                isSelected = selectedItem == BottomNavItem.SEARCH
            )
            
            // Calendar icon
            NavIcon(
                icon = "📅",
                isSelected = selectedItem == BottomNavItem.CALENDAR
            )
        }
    }
}

@Composable
private fun NavIcon(
    icon: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(
                color = if (isSelected) AynaColors.Purple else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
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
    BottomNavigation()
}
