package com.techtactoe.ayna.domain.model

/**
 * Represents a user's profile information
 */
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val phoneNumber: String = "",
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val favoriteServices: List<String> = emptyList(),
    val loyaltyPoints: Int = 0,
    val memberSince: Long = 0L // UTC Timestamp
)
