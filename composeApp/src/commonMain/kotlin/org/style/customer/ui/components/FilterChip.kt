package org.style.customer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun FilterChip(selected: Boolean, label: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = if (selected) Color.Black else Color.LightGray,
        modifier = Modifier.padding(end = 8.dp).clickable { onClick() }
    ) {
        Text(label, color = if (selected) Color.White else Color.Black, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
    }
} 