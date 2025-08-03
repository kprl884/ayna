package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.presentation.navigation.Screen
import com.techtactoe.ayna.presentation.navigation.icon

@Composable
fun AppBottomNavigation(
    currentScreen: Screen?,
    onItemSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(Screen.Home, Screen.Explore, Screen.Appointments, Screen.Profile)

    NavigationBar(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
        modifier = modifier.fillMaxWidth()
    ) {
        navItems.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onItemSelected(screen) },
                icon = {
                    screen.icon?.let {
                        Icon(imageVector = it, contentDescription = screen::class.simpleName)
                    }
                }
            )
        }
    }
}
