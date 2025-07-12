package org.style.customer.domain.models

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * User Domain Model
 * Represents a user in the system
 */
data class User @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val email: String,
    val name: String,
    val phoneNumber: String? = null,
    val profileImageUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
) 