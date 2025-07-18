package org.style.customer.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size

@Composable
fun RatingStars(rating: Float) {
    androidx.compose.foundation.layout.Row {
        repeat(5) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (it < rating) Color.Black else Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
} 