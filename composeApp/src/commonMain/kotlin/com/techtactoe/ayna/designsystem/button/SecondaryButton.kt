package com.techtactoe.ayna.designsystem.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Secondary Button Component following Ayna Design System
 * Outlined button for secondary actions
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    horizontalPadding: Dp = 24.dp,
    verticalPadding: Dp = 14.dp,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Medium
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = AynaShapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) borderColor else borderColor.copy(alpha = 0.5f)
        ),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = AynaTypography.labelLarge.copy(
                    fontSize = fontSize,
                    fontWeight = fontWeight
                ),
                color = if (enabled) contentColor else contentColor.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Text Button Component for tertiary actions
 */
@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Medium
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = AynaTypography.labelMedium.copy(
                fontSize = fontSize,
                fontWeight = fontWeight
            ),
            color = if (enabled) contentColor else contentColor.copy(alpha = 0.7f)
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    MaterialTheme {
        SecondaryButton(
            text = "Cancel",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun TextButtonPreview() {
    MaterialTheme {
        TextButton(
            text = "Forgot Password?",
            onClick = {}
        )
    }
}
