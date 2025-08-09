package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TimeSlotChip(
    epochMillis: Long,
    enabled: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ldt = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.currentSystemDefault())
    val bg = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(bg)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Text(
            text = ldt.hour.toString().padStart(2, '0') + ":" + ldt.minute.toString().padStart(2, '0'),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            ),
            color = color
        )
    }
}

@Composable
fun TimeSlotGrid(
    slots: List<Pair<Long, Boolean>>, // millis to enabled
    selected: Long?,
    onSelect: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 88.dp),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(slots.size) { idx ->
            val (millis, enabled) = slots[idx]
            TimeSlotChip(
                epochMillis = millis,
                enabled = enabled,
                selected = selected == millis,
                onClick = { if (enabled) onSelect(millis) }
            )
        }
    }
}
