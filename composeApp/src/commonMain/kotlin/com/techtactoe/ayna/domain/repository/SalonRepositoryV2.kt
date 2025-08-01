package com.techtactoe.ayna.domain.repository

import com.techtactoe.ayna.domain.model.SalonV2

/**
 * Repository interface for salon data operations
 */
interface SalonRepositoryV2 {
    /**
     * Get recommended salons for the user
     */
    suspend fun getRecommendedSalons(): List<SalonV2>
    
    /**
     * Search salons by query string
     */
    suspend fun searchSalons(query: String): List<SalonV2>
    
    /**
     * Get detailed information about a specific salon
     */
    suspend fun getSalonDetails(id: String): SalonV2?
    
    /**
     * Get salons near a specific location
     */
    suspend fun getSalonsNearLocation(latitude: Double, longitude: Double, radiusKm: Int = 10): List<SalonV2>
    
    /**
     * Get salons by category/service type
     */
    suspend fun getSalonsByCategory(category: String): List<SalonV2>
}
