package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
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
import com.techtactoe.ayna.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TeamSection(
    teamMembers: List<TeamMember>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Team",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AynaColors.Black
            )
            
            Text(
                text = "See all",
                fontSize = 16.sp,
                color = AynaColors.Purple
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(teamMembers) { member ->
                TeamMemberCard(member = member)
            }
        }
    }
}

@Composable
private fun TeamMemberCard(
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

@Preview
@Composable
fun TeamSectionPreview() {
    val mockTeam = listOf(
        TeamMember(
            id = "1",
            name = "Marios",
            role = "Creative Director",
            imageUrl = null,
            rating = 5.0
        ),
        TeamMember(
            id = "2",
            name = "Ankit",
            role = "Support Team",
            imageUrl = null,
            rating = 5.0
        ),
        TeamMember(
            id = "3",
            name = "Fanouria",
            role = "Stylist",
            imageUrl = null,
            rating = 5.0
        )
    )
    
    MaterialTheme {
        TeamSection(teamMembers = mockTeam)
    }
}
