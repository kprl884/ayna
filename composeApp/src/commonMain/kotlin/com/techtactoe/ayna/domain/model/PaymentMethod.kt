package com.techtactoe.ayna.domain.model

/**
 * Types of payment methods supported
 */
enum class PaymentMethodType {
    VISA,
    MASTERCARD,
    PAYPAL,
    APPLE_PAY,
    GOOGLE_PAY
}

/**
 * Represents a user's payment method
 */
data class PaymentMethod(
    val id: String,
    val type: PaymentMethodType,
    val lastFourDigits: String,
    val expiryDate: String = "",
    val isDefault: Boolean = false
)
