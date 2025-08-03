package com.techtactoe.ayna.data

import com.techtactoe.ayna.domain.model.*
import com.techtactoe.ayna.presentation.ui.screens.explore.sampleVenues

/**
 * Fake repository that immediately returns sample data without loading delays
 */
class FakeExploreRepository {
    
    private val venues = sampleVenues()
    
    suspend fun getVenues(
        filters: ExploreFilters,
        page: Int = 0,
        pageSize: Int = 20
    ): Result<List<Venue>> {
        return try {
            // No artificial delay - return data immediately
            
            var filteredVenues = venues
            
            // Apply search filter
            if (filters.searchQuery.isNotBlank()) {
                filteredVenues = filteredVenues.filter { venue ->
                    venue.name.contains(filters.searchQuery, ignoreCase = true) ||
                    venue.services.any { service -> 
                        service.name.contains(filters.searchQuery, ignoreCase = true) 
                    }
                }
            }
            
            // Apply venue type filter
            if (filters.venueType != VenueType.EVERYONE) {
                filteredVenues = filteredVenues.filter { it.venueType == filters.venueType }
            }
            
            // Apply price filter
            filteredVenues = filteredVenues.filter { venue ->
                venue.services.any { service ->
                    service.price >= filters.priceRange.min && 
                    service.price <= filters.priceRange.max
                }
            }
            
            // Apply sorting
            filteredVenues = when (filters.sortOption) {
                SortOption.RECOMMENDED -> filteredVenues.sortedByDescending { it.rating * it.reviewCount }
                SortOption.TOP_RATED -> filteredVenues.sortedByDescending { it.rating }
                SortOption.NEAREST -> filteredVenues
                else ->  filteredVenues.sortedByDescending { it.rating * it.reviewCount }
            }
            
            // Apply pagination
            val startIndex = page * pageSize
            val endIndex = minOf(startIndex + pageSize, filteredVenues.size)
            
            if (startIndex >= filteredVenues.size) {
                Result.success(emptyList())
            } else {
                Result.success(filteredVenues.subList(startIndex, endIndex))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun bookmarkVenue(venueId: String): Result<Boolean> {
        // No delay for instant feedback
        return Result.success(true)
    }
    
    fun getCities(): List<String> {
        return listOf("Istanbul", "Ankara", "Izmir", "Antalya", "Bursa")
    }
    
    fun getServiceCategories(): List<String> {
        return listOf("Hair & styling", "Nails", "Massage", "Skincare", "Makeup", "Barbershop")
    }
}
