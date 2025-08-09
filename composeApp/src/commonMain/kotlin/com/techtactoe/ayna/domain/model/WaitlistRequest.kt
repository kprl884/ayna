package com.techtactoe.ayna.domain.model

import kotlin.time.ExperimentalTime


/**
 * Represents a waitlist request when no time slots are available
 */
data class WaitlistRequest @OptIn(ExperimentalTime::class) constructor(
    val id: String = "",
    val userId: String,
    val salonId: String,
    val serviceId: String,
    val preferredDate: Long,
    val preferredTimeRange: String, // e.g., "Any time", "Morning", "Afternoon", "Evening"
    val status: WaitlistStatus = WaitlistStatus.PENDING,
    val createdAt: Long = kotlin.time.Clock.System.now().toEpochMilliseconds()
)