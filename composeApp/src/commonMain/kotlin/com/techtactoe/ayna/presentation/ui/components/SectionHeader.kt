package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.common.designsystem.theme.AynaShapes
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.theme.StringResources
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = AynaTypography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        actionText?.let {
            TextButton(
                onClick = onActionClick,
                shape = AynaShapes.small
            ) {
                Text(
                    text = it,
                    style = AynaTypography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun SectionHeaderPreview() {
    SectionHeader(
        title = StringResources.popular_text,
        actionText = StringResources.view_profile_button
    )
}
