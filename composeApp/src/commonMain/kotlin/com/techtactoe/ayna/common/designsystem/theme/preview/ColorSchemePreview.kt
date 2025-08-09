package com.techtactoe.ayna.common.designsystem.theme.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.techtactoe.ayna.common.designsystem.theme.AppTheme
import com.techtactoe.ayna.common.designsystem.theme.AynaTheme
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography

private data class RoleSwatch(val name: String, val bg: Color, val on: Color?)

@Composable
private fun ColorRow(item: RoleSwatch) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(item.bg)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(text = item.name, fontWeight = FontWeight.SemiBold)
            val hex = item.bg.value.toULong().toString(16).uppercase().padStart(8, '0')
            Text(text = "#" + hex)
        }
        if (item.on != null) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(item.on)
            )
        }
    }
}

@Composable
fun ColorSchemePreviewList() {
    val cs = MaterialTheme.colorScheme
    val extras = AynaTheme.extraColors
    val items = listOf(
        RoleSwatch("primary / onPrimary", cs.primary, cs.onPrimary),
        RoleSwatch("primaryContainer / onPrimaryContainer", cs.primaryContainer, cs.onPrimaryContainer),
        RoleSwatch("secondary / onSecondary", cs.secondary, cs.onSecondary),
        RoleSwatch("secondaryContainer / onSecondaryContainer", cs.secondaryContainer, cs.onSecondaryContainer),
        RoleSwatch("tertiary / onTertiary", cs.tertiary, cs.onTertiary),
        RoleSwatch("background / onBackground", cs.background, cs.onBackground),
        RoleSwatch("surface / onSurface", cs.surface, cs.onSurface),
        RoleSwatch("surfaceVariant / onSurfaceVariant", cs.surfaceVariant, cs.onSurfaceVariant),
        RoleSwatch("outline / outlineVariant", cs.outline, cs.outlineVariant),
        RoleSwatch("error / onError", cs.error, cs.onError),
        // Extras
        RoleSwatch("extra.border", extras.border, null),
        RoleSwatch("extra.inactive", extras.inactive, null),
        RoleSwatch("extra.success", extras.success, null),
        RoleSwatch("extra.warning", extras.warning, null),
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { ColorRow(it) }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppTheme(darkTheme = false, typography = AynaTypography) {
        ColorSchemePreviewList()
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true, typography = AynaTypography) {
        ColorSchemePreviewList()
    }
}
