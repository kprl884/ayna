package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
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
    val navItems = listOf(Screen.Home, Screen.Search, Screen.Appointments, Screen.Profile)

    NavigationBar(
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
                },
                label = { 
                    val label = when (screen) {
                        is Screen.Home -> "Home"
                        is Screen.Search -> "Search"
                        is Screen.Appointments -> "Appointments"
                        is Screen.Profile -> "Profile"
                        else -> screen::class.simpleName ?: "Unknown"
                    }
                    Text(text = label)
                }
            )
        }
    }
} 