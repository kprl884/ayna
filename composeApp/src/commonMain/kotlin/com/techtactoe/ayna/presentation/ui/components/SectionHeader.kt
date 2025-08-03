package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.designsystem.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = AynaColors.PrimaryText,
        modifier = modifier.padding(horizontal = 16.dp)
    )
}

@Preview
@Composable
fun SectionHeaderPreview() {
    SectionHeader(title = "Recommended")
}
