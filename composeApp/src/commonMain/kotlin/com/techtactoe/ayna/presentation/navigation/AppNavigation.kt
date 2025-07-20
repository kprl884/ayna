package com.techtactoe.ayna.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techtactoe.ayna.presentation.ui.screens.home.HomeScreen
import com.techtactoe.ayna.util.LogLevel
import com.techtactoe.ayna.util.log

@Composable
fun AppNavigation(
) {
    val navController = rememberNavController().apply {
        currentBackStack
            .collectAsStateWithLifecycle()
            .value
            .map { it.destination.route?.substringAfterLast('.') }
            .also { infoLog -> log(LogLevel.DEBUG, tag = "navbar", message = infoLog.toString()) }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onSalonClick = { salonId ->
                    navController.navigate(Screen.Detail.createRoute(salonId))
                }
            )
        }

        composable(Screen.Detail.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Detail Screen - Coming Soon!")
            }
        }
    }
} 