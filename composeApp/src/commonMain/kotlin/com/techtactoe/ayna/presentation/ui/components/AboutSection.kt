package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.theme.StringResources
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.OpeningHour
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class AboutSectionViewState(
    val description: String,
    val openingHours: List<OpeningHour>,
    val features: List<String> = listOf(StringResources.booking_confirmed_text)
)

@Composable
fun AboutSection(
    viewState: AboutSectionViewState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        SectionHeader(
            title = StringResources.about_text
        )

        // Description
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
        ) {
            Text(
                text = viewState.description,
                style = AynaTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Opening times
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            Text(
                text = "Opening times",
                style = AynaTypography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            viewState.openingHours.forEach { hour ->
                OpeningHourRow(hour = hour)
            }
        }

        // Features
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            Text(
                text = "Additional information",
                style = AynaTypography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            viewState.features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = feature,
                        style = AynaTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun OpeningHourRow(
    hour: OpeningHour,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            // Status indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (hour.isOpen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
            )

            Text(
                text = hour.day,
                style = AynaTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = if (hour.isOpen && hour.openTime != null && hour.closeTime != null) {
                "${hour.openTime} – ${hour.closeTime}"
            } else {
                "Closed"
            },
            style = AynaTypography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun AboutSectionPreview() {
    val mockViewState = AboutSectionViewState(
        description = "Hair Etc. Studio offers the most unique hair experience in Cyprus. We are a team of creators working with people on a daily basis...",
        openingHours = listOf(
            OpeningHour("Monday", false),
            OpeningHour("Tuesday", true, "9:00 AM", "7:00 PM"),
            OpeningHour("Wednesday", true, "9:00 AM", "7:00 PM"),
            OpeningHour("Thursday", true, "9:30 AM", "5:30 PM"),
            OpeningHour("Friday", true, "9:00 AM", "7:00 PM"),
            OpeningHour("Saturday", true, "8:30 AM", "5:00 PM"),
            OpeningHour("Sunday", false)
        ),
        features = listOf(StringResources.booking_confirmed_text, "Professional service")
    )

    MaterialTheme {
        AboutSection(
            viewState = mockViewState
        )
    }
}
