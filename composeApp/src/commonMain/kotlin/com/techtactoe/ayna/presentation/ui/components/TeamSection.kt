package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.TeamMember
import com.techtactoe.ayna.presentation.theme.AynaColors
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
                modifier = Modifier.clickable {
                    // todo navigatge professional screens
                },
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
