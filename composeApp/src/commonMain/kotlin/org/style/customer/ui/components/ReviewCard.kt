package org.style.customer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.style.customer.model.Review

@Composable
fun ReviewCard(review: Review) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Box(
            Modifier.size(40.dp).background(Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(review.initials, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(review.customerName, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                RatingStars(review.rating)
            }
            Text(review.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text(review.comment, style = MaterialTheme.typography.bodyMedium)
        }
    }
} 