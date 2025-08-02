package com.techtactoe.ayna.presentation.ui.screens.appointments.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.presentation.theme.AynaAppTheme
import com.techtactoe.ayna.presentation.theme.Spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PurpleCalendarIcon(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9C7BFF), // Lighter purple at top
                        Color(0xFF7B61FF)  // Brand purple at bottom
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Calendar icon structure
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top section with small rectangles (calendar binding)
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier.width(6.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(Color.White.copy(alpha = 0.9f))
                    )
                }
            }

            // Main calendar body
            Box(
                modifier = Modifier.size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.95f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Calendar grid dots
                    repeat(3) { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            repeat(3) { col ->
                                Box(
                                    modifier = Modifier.size(3.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(
                                            if (row == 1 && col == 1) Color(0xFF7B61FF)
                                            else Color(0xFFD1C4E9)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PurpleCalendarIconPreview() {
    AynaAppTheme {
        Surface(
            modifier = Modifier.padding(Spacing.lg)
        ) {
            PurpleCalendarIcon()
        }
    }
}