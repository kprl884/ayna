package com.techtactoe.ayna.domain.model

/**
 * Represents a waitlist request when no time slots are available
 */
data class WaitlistRequest(
    val id: String = "",
    val userId: String,
    val salonId: String,
    val serviceId: String,
    val preferredDate: Long,
    val preferredTimeRange: String, // e.g., "Any time", "Morning", "Afternoon", "Evening"
    val status: WaitlistStatus = WaitlistStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Status of a waitlist request
 */
enum class WaitlistStatus {
    PENDING,
    NOTIFIED,
    CONVERTED,
    EXPIRED
}
