package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.TeamMember
import com.techtactoe.ayna.designsystem.theme.AynaColors

@Composable
fun TeamMemberCard(
    member: TeamMember,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(AynaColors.LightGray),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for actual image
            Text(
                text = "👤",
                fontSize = 32.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Rating
        Text(
            text = "${member.rating} ⭐",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = AynaColors.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Name
        Text(
            text = member.name.uppercase(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = AynaColors.Black,
            textAlign = TextAlign.Center
        )

        // Role
        Text(
            text = member.role.uppercase(),
            fontSize = 10.sp,
            color = AynaColors.SecondaryText,
            textAlign = TextAlign.Center
        )
    }
}