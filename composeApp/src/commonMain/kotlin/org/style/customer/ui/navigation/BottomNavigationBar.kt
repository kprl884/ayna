package org.style.customer.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Bottom Navigation Bar Component
 * Follows Material Design 3 guidelines with proper sizing and spacing
 *
 * Specifications:
 * - Bottom bar height: 56dp (fixed)
 * - Icon size: 24dp x 24dp
 * - Text size: 12sp
 * - Padding (top/bottom): 8dp
 * - Minimum touch target: 48dp x 48dp
 */
@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemClick: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.Transparent,
        tonalElevation = 8.dp,
    ) {
        BottomNavItems.items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemClick(index) },
                icon = {
                    Icon(
                        imageVector = if (selectedIndex == index) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                )
            )
        }
    }
} 