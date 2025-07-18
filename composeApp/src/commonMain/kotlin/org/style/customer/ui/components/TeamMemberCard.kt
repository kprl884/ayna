package org.style.customer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.style.customer.model.TeamMember

@Composable
fun TeamMemberCard(member: TeamMember) {
    Column(
        Modifier.width(120.dp).padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Image loader
        Box(Modifier.size(64.dp).clip(CircleShape).background(Color.LightGray))
        Text(member.name, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(member.title, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        RatingStars(member.rating)
    }
} 