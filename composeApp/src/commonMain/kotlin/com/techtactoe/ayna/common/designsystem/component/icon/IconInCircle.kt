package com.techtactoe.ayna.common.designsystem.component.icon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconInCircle(
    onClick: () -> Unit,
    resource: DrawableResource,
    contentDescription: String? = null,
    shadowElevation: Dp = 2.dp
) {
    Surface(
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() },
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onPrimary,
        shadowElevation = shadowElevation
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(resource = resource),
                contentDescription = contentDescription,
            )
        }
    }
}

