package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import com.techtactoe.ayna.data.supabase.dto.SalonDto
import com.techtactoe.ayna.data.supabase.dto.ServiceDto
import com.techtactoe.ayna.data.supabase.dto.SalonImageDto
import com.techtactoe.ayna.domain.model.ExploreFilters
import com.techtactoe.ayna.domain.model.SortOption
import com.techtactoe.ayna.domain.model.Venue
import com.techtactoe.ayna.domain.model.VenueLocation
import com.techtactoe.ayna.domain.model.VenueService
import com.techtactoe.ayna.domain.model.VenueType
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Supabase implementation for Explore feature
 * Handles venue discovery with advanced filtering and search
 */
class SupabaseExploreRepositoryImpl {

    private val client = AynaSupabaseClient.database

    suspend fun getVenues(
        filters: ExploreFilters,
        page: Int = 0,
        pageSize: Int = 20
    ): List<Venue> {
        return try {
            var query = client
                .from("salons")
                .select(Columns.ALL)

            // Apply search filter
            if (filters.searchQuery.isNotBlank()) {
                query = query.textSearch("name", filters.searchQuery)
            }

            // Apply city filter
            if (filters.selectedCity.isNotBlank() && filters.selectedCity != "Istanbul") {
                query = query.eq("city", filters.selectedCity)
            }

            // Apply venue type filter
            if (filters.venueType != VenueType.EVERYONE) {
                query = query.eq("venue_type", filters.venueType.name)
            }

            // Apply sorting
            query = when (filters.sortOption) {
                SortOption.TOP_RATED -> query.order("rating", ascending = false)
                SortOption.NEAREST -> query.order("city", ascending = true) // TODO: Implement proper distance sorting
                SortOption.PRICE_LOW_TO_HIGH -> query.order("id", ascending = true) // TODO: Order by min service price
                SortOption.PRICE_HIGH_TO_LOW -> query.order("id", ascending = false) // TODO: Order by max service price
                else -> query.order("is_featured", ascending = false).order("rating", ascending = false)
            }

            // Apply pagination
            query = query.range(page * pageSize, (page + 1) * pageSize - 1)

            val salonsResponse = query.decodeList<SalonDto>()

            // Convert to Venue domain objects
            salonsResponse.map { salonDto ->
                coroutineScope {
                    val servicesDeferred = async { getServicesForSalon(salonDto.id, filters.priceRange) }
                    val imagesDeferred = async { getImagesForSalon(salonDto.id) }

                    val services = servicesDeferred.await()
                    val images = imagesDeferred.await()

                    Venue(
                        id = salonDto.id,
                        name = salonDto.name,
                        rating = salonDto.rating,
                        reviewCount = salonDto.reviewCount,
                        district = salonDto.district ?: "",
                        city = salonDto.city,
                        images = images,
                        services = services,
                        venueType = VenueType.valueOf(salonDto.venueType),
                        location = if (salonDto.latitude != null && salonDto.longitude != null) {
                            VenueLocation(
                                latitude = salonDto.latitude,
                                longitude = salonDto.longitude,
                                address = salonDto.address
                            )
                        } else null,
                        isBookmarkSaved = false // TODO: Check user favorites
                    )
                }
            }
        } catch (e: Exception) {
            println("Error fetching venues: ${e.message}")
            throw e
        }
    }

    suspend fun bookmarkVenue(venueId: String, userId: String): Result<Boolean> {
        return try {
            // Check if already bookmarked
            val existing = client
                .from("user_favorites")
                .select("id")
                .eq("user_id", userId)
                .eq("salon_id", venueId)
                .decodeSingleOrNull<Map<String, String>>()

            if (existing != null) {
                // Remove bookmark
                client
                    .from("user_favorites")
                    .delete()
                    .eq("user_id", userId)
                    .eq("salon_id", venueId)
            } else {
                // Add bookmark
                client
                    .from("user_favorites")
                    .insert(
                        mapOf(
                            "user_id" to userId,
                            "salon_id" to venueId
                        )
                    )
            }

            Result.success(true)
        } catch (e: Exception) {
            println("Error bookmarking venue: ${e.message}")
            Result.failure(e)
        }
    }

    fun getCities(): List<String> {
        // TODO: Implement dynamic city fetching from database
        return listOf("Istanbul", "Ankara", "Izmir", "Antalya", "Bursa", "Nicosia")
    }

    fun getServiceCategories(): List<String> {
        // TODO: Implement dynamic category fetching from database
        return listOf("Hair & styling", "Nails", "Massage", "Skincare", "Makeup", "Barbershop")
    }

    // Helper functions
    private suspend fun getServicesForSalon(
        salonId: String,
        priceRange: com.techtactoe.ayna.domain.model.PriceRange
    ): List<VenueService> {
        return try {
            val servicesResponse = client
                .from("services")
                .select(Columns.ALL)
                .eq("salon_id", salonId)
                .eq("is_active", true)
                .gte("price_cents", priceRange.min)
                .lte("price_cents", priceRange.max)
                .order("price_cents", ascending = true)
                .decodeList<ServiceDto>()

            servicesResponse.map { serviceDto ->
                VenueService(
                    id = serviceDto.id,
                    name = serviceDto.name,
                    price = serviceDto.priceCents,
                    duration = serviceDto.durationMinutes,
                    category = serviceDto.description ?: "General"
                )
            }
        } catch (e: Exception) {
            println("Error fetching services for salon $salonId: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getImagesForSalon(salonId: String): List<String> {
        return try {
            client
                .from("salon_images")
                .select("image_url")
                .eq("salon_id", salonId)
                .order("sort_order", ascending = true)
                .decodeList<SalonImageDto>()
                .map { it.imageUrl }
        } catch (e: Exception) {
            println("Error fetching images for salon $salonId: ${e.message}")
            emptyList()
        }
    }
}