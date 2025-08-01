package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.designsystem.button.PrimaryButton
import com.techtactoe.ayna.presentation.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FloatingBookingBar(
    serviceCount: Int,
    onBookNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = AynaColors.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$serviceCount services available",
                style = MaterialTheme.typography.bodyMedium,
                color = AynaColors.SecondaryText
            )

            PrimaryButton(
                text = "Book now",
                onClick = onBookNowClick
            )
        }
    }
}

@Preview
@Composable
fun FloatingBookingBarPreview() {
    MaterialTheme {
        FloatingBookingBar(
            serviceCount = 51,
            onBookNowClick = {}
        )
    }
}
