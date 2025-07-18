package org.style.customer.domain.models

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Business Domain Model
 * Represents a business/service provider in the system
 */
data class Business @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val name: String,
    val description: String,
    val category: BusinessCategory,
    val address: Address,
    val phoneNumber: String,
    val email: String,
    val website: String? = null,
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val images: List<String> = emptyList(),
    val isVerified: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
)

/**
 * Address Model
 */
data class Address(
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String = "Turkey",
    val latitude: Double? = null,
    val longitude: Double? = null
) 