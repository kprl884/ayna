package org.style.customer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.style.customer.model.TeamMember
import org.style.customer.ui.theme.Spacing
import org.style.customer.ui.theme.StringResources

@Composable
fun StaffSelectionSection(
    availableStaff: List<TeamMember>,
    selectedStaff: TeamMember?,
    onStaffSelected: (TeamMember?) -> Unit
) {
    Column {
        Text(
            text = StringResources.select_staff_text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(Spacing.md)
            ) {
                // No preference option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedStaff == null,
                        onClick = { onStaffSelected(null) },
                        colors = RadioButtonDefaults.colors(
                            MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.width(Spacing.sm))

                    Column {
                        Text(
                            text = StringResources.no_preference_text,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = StringResources.any_stylist_text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (availableStaff.isNotEmpty()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = Spacing.sm),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                // Staff members
                availableStaff.forEach { staff ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedStaff?.id == staff.id,
                            onClick = { onStaffSelected(staff) },
                            colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                        )

                        Spacer(modifier = Modifier.width(Spacing.sm))

                        Column {
                            Text(
                                text = staff.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = staff.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
} 