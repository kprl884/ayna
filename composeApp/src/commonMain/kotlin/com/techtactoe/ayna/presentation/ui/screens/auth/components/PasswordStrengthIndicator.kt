package com.techtactoe.ayna.presentation.ui.screens.auth.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import com.techtactoe.ayna.data.auth.AuthValidationUtils
import com.techtactoe.ayna.data.auth.PasswordStrengthLevel

@Composable
fun PasswordStrengthIndicator(
    password: String,
    modifier: Modifier = Modifier
) {
    val strength = AuthValidationUtils.getPasswordStrength(password)
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        // Strength bars
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
        ) {
            repeat(5) { index ->
                val isActive = index < when (strength.level) {
                    PasswordStrengthLevel.VERY_WEAK -> 1
                    PasswordStrengthLevel.WEAK -> 2
                    PasswordStrengthLevel.MEDIUM -> 3
                    PasswordStrengthLevel.STRONG -> 4
                    PasswordStrengthLevel.VERY_STRONG -> 5
                }
                
                val barColor by animateColorAsState(
                    targetValue = if (isActive) strength.color else MaterialTheme.colorScheme.outline,
                    animationSpec = tween(durationMillis = 300),
                    label = "password_strength_color"
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(barColor)
                )
            }
        }
        
        // Strength text
        if (password.isNotEmpty()) {
            Text(
                text = "Password strength: ${strength.text}",
                style = AynaTypography.bodySmall,
                color = strength.color
            )
            
            // Feedback
            if (strength.feedback.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(top = Spacing.extraSmall),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    strength.feedback.forEach { feedback ->
                        Text(
                            text = "• $feedback",
                            style = AynaTypography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}