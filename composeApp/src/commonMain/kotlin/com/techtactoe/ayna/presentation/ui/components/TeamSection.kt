package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.domain.model.TeamMember
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class TeamSectionViewState(
    val teamMembers: List<TeamMember>
)

@Composable
fun TeamSection(
    viewState: TeamSectionViewState,
    onSeeAllClick: () -> Unit = {},
    onTeamMemberClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        SectionHeader(
            title = "Team",
            actionText = "See all",
            onActionClick = onSeeAllClick
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(viewState.teamMembers) { member ->
                TeamMemberCard(
                    viewState = TeamMemberCardViewState(
                        id = member.id,
                        name = member.name,
                        role = member.role,
                        imageUrl = member.imageUrl,
                        rating = member.rating
                    ),
                    onClick = { onTeamMemberClick(member.id) }
                )
            }
        }
    }
}


@Preview
@Composable
fun TeamSectionPreview() {
    val mockViewState = TeamSectionViewState(
        teamMembers = listOf(
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
    )

    MaterialTheme {
        TeamSection(viewState = mockViewState)
    }
}
