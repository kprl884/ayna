package org.style.customer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import org.style.customer.model.Service

@Composable
fun ServiceCard(service: Service, onBook: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(service.name, style = MaterialTheme.typography.titleMedium)
                Text("${service.duration}${if (service.femaleOnly) " • Female only" else ""}", style = MaterialTheme.typography.bodySmall)
                Text(service.price, style = MaterialTheme.typography.bodyMedium)
            }
            OutlinedButton(onClick = onBook) { Text("Book") }
        }
    }
} 