package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OpeningHour(
    val day: String,
    val isOpen: Boolean,
    val openTime: String? = null,
    val closeTime: String? = null
)