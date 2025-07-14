package org.style.customer.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.style.customer.ui.designsystem.components.text.AppText
import org.style.customer.ui.designsystem.foundation.typography.AppTextStyles

class SearchScreen : Screen {
    @Composable
    override fun Content() {
        val systemPadding = WindowInsets.statusBars.asPaddingValues()
        
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = systemPadding.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AppText(
                    text = "Arama Sayfası",
                    style = AppTextStyles.businessName,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
} 