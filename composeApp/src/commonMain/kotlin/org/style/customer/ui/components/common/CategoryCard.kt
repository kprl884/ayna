package org.style.customer.ui.components.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Aperture
import compose.icons.feathericons.Award
import compose.icons.feathericons.Cast
import compose.icons.feathericons.CloudRain
import compose.icons.feathericons.Codepen
import compose.icons.feathericons.CornerLeftDown
import compose.icons.feathericons.CreditCard
import compose.icons.feathericons.Database
import compose.icons.feathericons.Home

@Composable
fun CategoryCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "scale"
    )

    Card(
        modifier = modifier
            .size(width = 160.dp, height = 80.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

fun getCategoryIcon(categoryName: String): ImageVector {
    return when (categoryName.lowercase()) {
        "saç" -> FeatherIcons.Home
        "makyaj" -> FeatherIcons.Award
        "cilt bakımı" -> FeatherIcons.CreditCard
        "manikür" -> FeatherIcons.CloudRain
        "pedikür" -> FeatherIcons.Codepen
        "masaj" -> FeatherIcons.Database
        "epilasyon" -> FeatherIcons.CornerLeftDown
        "kaş & kirpik" -> FeatherIcons.Cast
        else -> FeatherIcons.Aperture
    }
}

fun getCategoryColor(categoryName: String): Color {
    return when (categoryName.lowercase()) {
        "saç" -> Color(0xFFE3F2FD) // Açık mavi
        "makyaj" -> Color(0xFFFCE4EC) // Açık pembe
        "cilt bakımı" -> Color(0xFFE8F5E8) // Açık yeşil
        "manikür" -> Color(0xFFFFF3E0) // Açık turuncu
        "pedikür" -> Color(0xFFF3E5F5) // Açık mor
        "masaj" -> Color(0xFFE0F2F1) // Açık turkuaz
        "epilasyon" -> Color(0xFFFFEBEE) // Açık kırmızı
        "kaş & kirpik" -> Color(0xFFF1F8E9) // Açık yeşil
        else -> Color(0xFFF5F5F5) // Açık gri
    }
} 