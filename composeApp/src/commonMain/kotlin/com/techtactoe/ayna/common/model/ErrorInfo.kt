package com.techtactoe.ayna.common.model

/**
 * Data class to hold error information for better error handling
 */
data class ErrorInfo(
    val title: String,
    val message: String,
    val primaryAction: Pair<String, () -> Unit>,
    val secondaryAction: Pair<String, () -> Unit>? = null
)