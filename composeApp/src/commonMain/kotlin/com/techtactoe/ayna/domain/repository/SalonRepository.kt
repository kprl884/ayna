package com.techtactoe.ayna.domain.repository

import com.techtactoe.ayna.domain.model.Salon

/**
 * Repository interface for salon data operations
 */
interface SalonRepository {

    suspend fun getNearbySalons(): List<Salon>

    /**
     * Get recommended salons for the user
     */
    suspend fun getRecommendedSalons(): List<Salon>

    /**
     * Search salons by query string
     */
    suspend fun searchSalons(query: String): List<Salon>

    /**
     * Get detailed information about a specific salon
     */
    suspend fun getSalonDetails(id: String): Salon?

    /**
     * Get salons near a specific location
     */
    suspend fun getSalonsNearLocation(
        latitude: Double,
        longitude: Double,
        radiusKm: Int = 10
    ): List<Salon>

    /**
     * Get salons by category/service type
     */
    suspend fun getSalonsByCategory(category: String): List<Salon>
}
