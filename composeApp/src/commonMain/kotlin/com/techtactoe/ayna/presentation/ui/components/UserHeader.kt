package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserHeader(
    userName: String,
    userInitials: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Greeting text
        Text(
            text = "Hey, $userName",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = AynaColors.PrimaryText
        )

        // User avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = AynaColors.LightGray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userInitials,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AynaColors.PrimaryText
            )
        }
    }
}

@Preview
@Composable
fun UserHeaderPreview() {
    UserHeader(
        userName = "John",
        userInitials = "JS"
    )
}
