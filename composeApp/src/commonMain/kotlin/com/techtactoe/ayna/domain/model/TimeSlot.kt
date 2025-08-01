package com.techtactoe.ayna.domain.model

/**
 * Represents a time slot for booking appointments
 */
data class TimeSlot(
    val dateTime: Long, // UTC Timestamp
    val isAvailable: Boolean,
    val formattedTime: String = "" // e.g., "10:30 AM"
)
