package org.style.customer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.style.customer.model.Service
import org.style.customer.ui.theme.Spacing
import org.style.customer.ui.theme.StringResources

@Composable
fun SelectedServicesSection(
    services: List<Service>,
    onServiceRemove: (Service) -> Unit
) {
    Column {
        Text(
            text = StringResources.services_text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        services.forEach { service ->
            ServiceItem(
                service = service,
                onRemove = { onServiceRemove(service) }
            )

            if (service != services.last()) {
                HorizontalDivider(
                    Modifier.padding(vertical = Spacing.xs),
                    DividerDefaults.Thickness, MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}