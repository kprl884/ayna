package com.techtactoe.ayna.domain.repository

import com.techtactoe.ayna.domain.model.PaymentMethod
import com.techtactoe.ayna.domain.model.UserProfile

/**
 * Repository interface for user profile data operations
 */
interface ProfileRepository {
    /**
     * Get user profile by ID
     */
    suspend fun getUserProfile(userId: String): UserProfile?
    
    /**
     * Update user profile information
     */
    suspend fun updateUserProfile(profile: UserProfile): Boolean
    
    /**
     * Add a payment method to user profile
     */
    suspend fun addPaymentMethod(userId: String, paymentMethod: PaymentMethod): Boolean
    
    /**
     * Remove a payment method from user profile
     */
    suspend fun removePaymentMethod(userId: String, paymentMethodId: String): Boolean
    
    /**
     * Update user's favorite services
     */
    suspend fun updateFavoriteServices(userId: String, serviceIds: List<String>): Boolean
}
