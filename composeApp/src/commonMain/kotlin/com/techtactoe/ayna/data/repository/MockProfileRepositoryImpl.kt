package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.domain.model.PaymentMethod
import com.techtactoe.ayna.domain.model.PaymentMethodType
import com.techtactoe.ayna.domain.model.UserProfile
import com.techtactoe.ayna.domain.repository.ProfileRepository
import kotlinx.coroutines.delay

/**
 * Mock implementation of ProfileRepository with realistic hardcoded data
 */
class MockProfileRepositoryImpl : ProfileRepository {
    
    private var userProfile = UserProfile(
        id = "user123",
        name = "Alparslan Köprülü",
        email = "alparslan@example.com",
        profilePictureUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400",
        phoneNumber = "+90 532 123 4567",
        paymentMethods = listOf(
            PaymentMethod(
                id = "pm1",
                type = PaymentMethodType.VISA,
                lastFourDigits = "4321",
                expiryDate = "12/25",
                isDefault = true
            ),
            PaymentMethod(
                id = "pm2",
                type = PaymentMethodType.MASTERCARD,
                lastFourDigits = "8765",
                expiryDate = "08/26",
                isDefault = false
            )
        ),
        favoriteServices = listOf("svc1", "svc7", "svc10"),
        loyaltyPoints = 1250,
        memberSince = System.currentTimeMillis() - (365 * 24 * 60 * 60 * 1000L) // 1 year ago
    )
    
    override suspend fun getUserProfile(userId: String): UserProfile? {
        delay(800) // Simulate network latency
        return if (userId == userProfile.id) userProfile else null
    }
    
    override suspend fun updateUserProfile(profile: UserProfile): Boolean {
        delay(1000) // Simulate network latency
        return try {
            userProfile = profile
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun addPaymentMethod(userId: String, paymentMethod: PaymentMethod): Boolean {
        delay(1200) // Simulate network latency
        return try {
            if (userId == userProfile.id) {
                val updatedPaymentMethods = userProfile.paymentMethods.toMutableList()
                updatedPaymentMethods.add(paymentMethod)
                userProfile = userProfile.copy(paymentMethods = updatedPaymentMethods)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun removePaymentMethod(userId: String, paymentMethodId: String): Boolean {
        delay(800) // Simulate network latency
        return try {
            if (userId == userProfile.id) {
                val updatedPaymentMethods = userProfile.paymentMethods.filter { it.id != paymentMethodId }
                userProfile = userProfile.copy(paymentMethods = updatedPaymentMethods)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun updateFavoriteServices(userId: String, serviceIds: List<String>): Boolean {
        delay(600) // Simulate network latency
        return try {
            if (userId == userProfile.id) {
                userProfile = userProfile.copy(favoriteServices = serviceIds)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
