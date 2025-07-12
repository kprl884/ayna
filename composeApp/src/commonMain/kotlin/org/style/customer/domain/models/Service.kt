package org.style.customer.domain.models

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Service Domain Model
 * Represents a service offered by a business
 */
data class Service @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val businessId: String,
    val name: String,
    val description: String,
    val duration: Int, // in minutes
    val price: Double,
    val currency: String = "TRY",
    val category: ServiceCategory,
    val isActive: Boolean = true,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now()
)

/**
 * Service Category Enum
 */
enum class ServiceCategory {
    HAIRCUT,
    HAIR_COLORING,
    MANICURE,
    PEDICURE,
    FACIAL,
    MASSAGE,
    WAXING,
    MAKEUP,
    DENTAL_CLEANING,
    FITNESS_TRAINING,
    OTHER
}