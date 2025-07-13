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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.FeatherIcons
import compose.icons.feathericons.CloudRain
import org.style.customer.ui.screens.home.HomeScreen

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
        // Remove `by` and directly access `.value`
        val progress = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        ).value

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
                // Logo/Icon with scale animation
                Icon(
                    imageVector = FeatherIcons.CloudRain,
                    contentDescription = "Ayna Logo",
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // App name with fade animation
                Text(
                    text = "Ayna",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Güzellik & Wellness",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp
                    ),
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Auto-navigate to HomeScreen after 3 seconds
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(3000)
            navigator.replace(HomeScreen())
        }
    }
}

/**
 * Linearly interpolate between two color lists
 */
fun lerpColors(colors1: List<Color>, colors2: List<Color>, t: Float): List<Color> =
    colors1.zip(colors2) { c1, c2 -> lerpColor(c1, c2, t) }

fun lerpColor(a: Color, b: Color, t: Float): Color =
    Color(
        red = lerp(a.red, b.red, t),
        green = lerp(a.green, b.green, t),
        blue = lerp(a.blue, b.blue, t),
        alpha = lerp(a.alpha, b.alpha, t)
    )

fun lerp(start: Float, stop: Float, fraction: Float): Float = start + (stop - start) * fraction