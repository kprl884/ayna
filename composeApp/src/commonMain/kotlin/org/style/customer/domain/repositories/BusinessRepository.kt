package org.style.customer.domain.repositories

import org.style.customer.domain.models.Business
import org.style.customer.domain.models.BusinessCategory

/**
 * Business Repository Interface
 * Handles business-related operations
 */
interface BusinessRepository {
    /**
     * Get businesses by category
     */
    suspend fun getBusinessesByCategory(category: BusinessCategory): Result<List<Business>>
    
    /**
     * Get businesses near location
     */
    suspend fun getBusinessesNearLocation(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): Result<List<Business>>
    
    /**
     * Get business by ID
     */
    suspend fun getBusinessById(businessId: String): Result<Business>
    
    /**
     * Search businesses
     */
    suspend fun searchBusinesses(query: String): Result<List<Business>>
    
    /**
     * Get featured businesses
     */
    suspend fun getFeaturedBusinesses(): Result<List<Business>>
    
    /**
     * Get businesses by rating
     */
    suspend fun getBusinessesByRating(minRating: Float): Result<List<Business>>
    
    /**
     * Follow a business
     */
    suspend fun followBusiness(businessId: String): Result<Unit>
    
    /**
     * Unfollow a business
     */
    suspend fun unfollowBusiness(businessId: String): Result<Unit>
    
    /**
     * Get followed businesses
     */
    suspend fun getFollowedBusinesses(): Result<List<Business>>
} 