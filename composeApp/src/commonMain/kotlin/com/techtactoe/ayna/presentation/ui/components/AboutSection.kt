package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.OpeningHour
import com.techtactoe.ayna.domain.model.SalonAbout
import com.techtactoe.ayna.designsystem.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutSection(
    about: SalonAbout,
    openingHours: List<OpeningHour>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "About",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AynaColors.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Description
        Text(
            text = about.description,
            fontSize = 14.sp,
            color = AynaColors.Black,
            lineHeight = 20.sp
        )
        
        Text(
            text = "Read more",
            fontSize = 14.sp,
            color = AynaColors.Purple,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )
        
        // Opening times
        Text(
            text = "Opening times",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AynaColors.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        openingHours.forEach { hour ->
            OpeningHourRow(hour = hour)
            
            if (hour != openingHours.last()) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Additional information
        Text(
            text = "Additional information",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AynaColors.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✓",
                fontSize = 16.sp,
                color = Color.Green,
                modifier = Modifier.padding(end = 8.dp)
            )
            
            Text(
                text = "Instant confirmation",
                fontSize = 14.sp,
                color = AynaColors.Black
            )
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (hour.isOpen) Color.Green else AynaColors.InactiveGray,
                        shape = CircleShape
                    )
            )
            
            Spacer(modifier = Modifier.size(8.dp))
            
            Text(
                text = hour.day,
                fontSize = 14.sp,
                color = AynaColors.Black
            )
        }
        
        Text(
            text = if (hour.isOpen && hour.openTime != null && hour.closeTime != null) {
                "${hour.openTime} – ${hour.closeTime}"
            } else {
                "Closed"
            },
            fontSize = 14.sp,
            color = AynaColors.SecondaryText
        )
    }
}

@Preview
@Composable
fun AboutSectionPreview() {
    val mockAbout = SalonAbout(
        description = "Hair Etc. Studio offers the most unique hair experience in Cyprus. We are a team of creators working with people on a daily basis...",
        fullDescription = "Full description would go here..."
    )
    
    val mockOpeningHours = listOf(
        OpeningHour("Monday", false),
        OpeningHour("Tuesday", true, "9:00 AM", "7:00 PM"),
        OpeningHour("Wednesday", true, "9:00 AM", "7:00 PM"),
        OpeningHour("Thursday", true, "9:30 AM", "5:30 PM"),
        OpeningHour("Friday", true, "9:00 AM", "7:00 PM"),
        OpeningHour("Saturday", true, "8:30 AM", "5:00 PM"),
        OpeningHour("Sunday", false)
    )
    
    MaterialTheme {
        AboutSection(
            about = mockAbout,
            openingHours = mockOpeningHours
        )
    }
}
