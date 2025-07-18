package org.style.customer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.style.customer.model.OpeningHours

@Composable
fun OpeningHoursRow(hours: OpeningHours) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(hours.day)
        Text(hours.hours, color = if (hours.isOpen) Color.Black else Color.Gray)
    }
} 