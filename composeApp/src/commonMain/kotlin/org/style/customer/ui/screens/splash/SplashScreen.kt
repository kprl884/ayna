package org.style.customer.ui.screens.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.style.customer.ui.designsystem.components.text.AppTitle
import org.style.customer.ui.designsystem.components.text.AppSubtitle
import org.style.customer.ui.navigation.MainScreen

/**
 * SplashScreen with animated gradient background and logo/text
 */
class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Animate gradient colors
        val colors1 = listOf(Color(0xFF037AFF), Color(0xFF0D1619), Color(0xFFB2EBF2))
        val colors2 = listOf(Color(0xFF0D1619), Color(0xFF037AFF), Color(0xFFB2EBF2))
        val infiniteTransition = rememberInfiniteTransition(label = "splash-gradient")
        
        val progress by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        val gradientBrush = Brush.linearGradient(
            colors = if (progress < 0.5f) colors1 else colors2
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                AppTitle(
                    text = "Ayna",
                    color = Color.White
                )
            }
        }

        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(3000)
            navigator.replace(MainScreen())
        }
    }
}