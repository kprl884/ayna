package com.techtactoe.ayna.presentation.ui.screens.salon.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.SalonDetail
import com.techtactoe.ayna.domain.model.SalonStatus
import com.techtactoe.ayna.common.designsystem.theme.AynaColors
import com.techtactoe.ayna.common.designsystem.theme.AynaColors.LightPurple

@Composable
fun SalonBasicInfo(
    salonDetail: SalonDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Salon name
        Text(
            text = salonDetail.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AynaColors.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Rating and reviews
        Text(
            text = "${salonDetail.rating} ⭐⭐⭐⭐⭐ (${salonDetail.reviewCount})",
            fontSize = 16.sp,
            color = AynaColors.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Address
        Text(
            text = salonDetail.address,
            fontSize = 14.sp,
            color = AynaColors.SecondaryText
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Status
        val statusText = when (salonDetail.status) {
            SalonStatus.OPEN -> "Open now"
            SalonStatus.CLOSED -> "Closed"
            SalonStatus.OPENS_LATER -> "Closed - opens on Tuesday at 9:00 AM"
        }

        Text(
            text = statusText,
            fontSize = 14.sp,
            color = AynaColors.SecondaryText
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Featured tag
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .border(
                    1.dp,
                    LightPurple,
                    MaterialTheme.shapes.medium
                )

        ) {
            Text(
                text = "Featured",
                fontSize = 14.sp,
                color = AynaColors.Purple,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}
