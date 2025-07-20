package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A utility composable that provides consistent safe area handling across platforms.
 * Automatically applies appropriate padding for status bars and navigation bars.
 */
@Composable
fun SafeAreaBox(
    modifier: Modifier = Modifier,
    applyStatusBarPadding: Boolean = true,
    applyNavigationBarPadding: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .then(
                if (applyStatusBarPadding) {
                    Modifier.windowInsetsPadding(WindowInsets.statusBars)
                } else {
                    Modifier
                }
            )
            .then(
                if (applyNavigationBarPadding) {
                    Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                } else {
                    Modifier
                }
            ),
        content = content
    )
}
