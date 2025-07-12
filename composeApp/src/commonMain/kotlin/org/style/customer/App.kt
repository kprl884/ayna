package org.style.customer

import androidx.compose.runtime.Composable
import org.style.customer.ui.navigation.AynaAppNavigation
import org.style.customer.ui.theme.AynaAppTheme

@Composable
fun App() {
    AynaAppTheme {
        AynaAppNavigation()
    }
}