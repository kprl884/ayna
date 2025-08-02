package com.techtactoe.ayna.presentation.ui.screens.appointments.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.techtactoe.ayna.presentation.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.theme.Spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptyAppointmentsContent(
    onSearchSalonsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PurpleCalendarIcon(
            modifier = Modifier.padding(bottom = Spacing.xl)
        )

        Text(
            text = "No appointments",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.md)
        )

        Text(
            text = "Your upcoming and past appointments\nwill appear here when you book",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.xxl)
        )

        OutlinedButton(
            onClick = onSearchSalonsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.xl),
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                "Search salons",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = Spacing.xs)
            )
        }
    }
}

@Preview
@Composable
private fun EmptyAppointmentsScreenPreview() {
    AynaAppTheme {
        EmptyAppointmentsContent(onSearchSalonsClick = {})
    }
}