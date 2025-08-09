package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import com.techtactoe.ayna.data.supabase.dto.SalonDto
import com.techtactoe.ayna.data.supabase.dto.ServiceDto
import com.techtactoe.ayna.data.supabase.dto.EmployeeDto
import com.techtactoe.ayna.data.supabase.dto.SalonImageDto
import com.techtactoe.ayna.data.supabase.dto.OpeningHourDto
import com.techtactoe.ayna.data.supabase.mapper.toDomain
import com.techtactoe.ayna.domain.model.DayOfWeek
import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.repository.SalonRepository
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Supabase implementation of SalonRepository
 * Handles all salon-related data operations with proper error handling
 */
class SupabaseSalonRepositoryImpl : SalonRepository {

    private val client = AynaSupabaseClient.database

    override suspend fun getNearbySalons(): List<Salon> {
        return try {
            val salonsResponse = client
                .from("salons")
                .select(Columns.ALL)
                .decodeList<SalonDto>()

            // Fetch related data for each salon
            salonsResponse.map { salonDto ->
                coroutineScope {
                    val servicesDeferred = async { getServicesForSalon(salonDto.id) }
                    val employeesDeferred = async { getEmployeesForSalon(salonDto.id) }
                    val imagesDeferred = async { getImagesForSalon(salonDto.id) }
                    val hoursDeferred = async { getOpeningHoursForSalon(salonDto.id) }

                    val services = servicesDeferred.await()
                    val employees = employeesDeferred.await()
                    val images = imagesDeferred.await()
                    val hours = hoursDeferred.await()

                    salonDto.toDomain(
                        services = services,
                        employees = employees,
                        images = images,
                        operatingHours = hours
                    )
                }
            }
        } catch (e: Exception) {
            println("Error fetching nearby salons: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecommendedSalons(): List<Salon> {
        return try {
            val salonsResponse = client
                .from("salons")
                .select(Columns.ALL)
                .eq("is_featured", true)
                .order("rating", ascending = false)
                .limit(10)
                .decodeList<SalonDto>()

            // Fetch related data for each salon
            salonsResponse.map { salonDto ->
                coroutineScope {
                    val servicesDeferred = async { getServicesForSalon(salonDto.id) }
                    val employeesDeferred = async { getEmployeesForSalon(salonDto.id) }
                    val imagesDeferred = async { getImagesForSalon(salonDto.id) }
                    val hoursDeferred = async { getOpeningHoursForSalon(salonDto.id) }

                    val services = servicesDeferred.await()
                    val employees = employeesDeferred.await()
                    val images = imagesDeferred.await()
                    val hours = hoursDeferred.await()

                    salonDto.toDomain(
                        services = services,
                        employees = employees,
                        images = images,
                        operatingHours = hours
                    )
                }
            }
        } catch (e: Exception) {
            println("Error fetching recommended salons: ${e.message}")
            throw e
        }
    }

    override suspend fun searchSalons(query: String): List<Salon> {
        return try {
            val salonsResponse = client
                .from("salons")
                .select(Columns.ALL)
                .textSearch("name", query)
                .decodeList<SalonDto>()

            // Also search in services
            val serviceResults = client
                .from("services")
                .select("salon_id")
                .textSearch("name", query)
                .decodeList<Map<String, String>>()

            val salonIdsFromServices = serviceResults.mapNotNull { it["salon_id"] }

            // Combine results
            val allSalonIds = (salonsResponse.map { it.id } + salonIdsFromServices).distinct()

            val allSalons = if (allSalonIds.isNotEmpty()) {
                client
                    .from("salons")
                    .select(Columns.ALL)
                    .`in`("id", allSalonIds)
                    .decodeList<SalonDto>()
            } else {
                salonsResponse
            }

            allSalons.map { salonDto ->
                coroutineScope {
                    val servicesDeferred = async { getServicesForSalon(salonDto.id) }
                    val employeesDeferred = async { getEmployeesForSalon(salonDto.id) }
                    val imagesDeferred = async { getImagesForSalon(salonDto.id) }
                    val hoursDeferred = async { getOpeningHoursForSalon(salonDto.id) }

                    val services = servicesDeferred.await()
                    val employees = employeesDeferred.await()
                    val images = imagesDeferred.await()
                    val hours = hoursDeferred.await()

                    salonDto.toDomain(
                        services = services,
                        employees = employees,
                        images = images,
                        operatingHours = hours
                    )
                }
            }
        } catch (e: Exception) {
            println("Error searching salons: ${e.message}")
            throw e
        }
    }

    override suspend fun getSalonDetails(id: String): Salon? {
        return try {
            val salonResponse = client
                .from("salons")
                .select(Columns.ALL)
                .eq("id", id)
                .decodeSingleOrNull<SalonDto>()

            salonResponse?.let { salonDto ->
                coroutineScope {
                    val servicesDeferred = async { getServicesForSalon(salonDto.id) }
                    val employeesDeferred = async { getEmployeesForSalon(salonDto.id) }
                    val imagesDeferred = async { getImagesForSalon(salonDto.id) }
                    val hoursDeferred = async { getOpeningHoursForSalon(salonDto.id) }

                    val services = servicesDeferred.await()
                    val employees = employeesDeferred.await()
                    val images = imagesDeferred.await()
                    val hours = hoursDeferred.await()

                    salonDto.toDomain(
                        services = services,
                        employees = employees,
                        images = images,
                        operatingHours = hours
                    )
                }
            }
        } catch (e: Exception) {
            println("Error fetching salon details: ${e.message}")
            null
        }
    }

    override suspend fun getSalonsNearLocation(
        latitude: Double,
        longitude: Double,
        radiusKm: Int
    ): List<Salon> {
        return try {
            // Using PostGIS for geographic queries
            val salonsResponse = client
                .from("salons")
                .select(Columns.ALL)
                .rpc(
                    "salons_within_radius",
                    mapOf(
                        "lat" to latitude,
                        "lng" to longitude,
                        "radius_km" to radiusKm
                    )
                )
                .decodeList<SalonDto>()

            salonsResponse.map { salonDto ->
                coroutineScope {
                    val servicesDeferred = async { getServicesForSalon(salonDto.id) }
                    val employeesDeferred = async { getEmployeesForSalon(salonDto.id) }
                    val imagesDeferred = async { getImagesForSalon(salonDto.id) }
                    val hoursDeferred = async { getOpeningHoursForSalon(salonDto.id) }

                    val services = servicesDeferred.await()
                    val employees = employeesDeferred.await()
                    val images = imagesDeferred.await()
                    val hours = hoursDeferred.await()

                    salonDto.toDomain(
                        services = services,
                        employees = employees,
                        images = images,
                        operatingHours = hours
                    )
                }
            }
        } catch (e: Exception) {
            println("Error fetching salons near location: ${e.message}")
            throw e
        }
    }

    override suspend fun getSalonsByCategory(category: String): List<Salon> {
        return try {
            // First get services in the category
            val serviceResults = client
                .from("services")
                .select("salon_id")
                .eq("category_id", category)
                .decodeList<Map<String, String>>()

            val salonIds = serviceResults.mapNotNull { it["salon_id"] }

            if (salonIds.isEmpty()) {
                return emptyList()
            }

            val salonsResponse = client
                .from("salons")
                .select(Columns.ALL)
                .`in`("id", salonIds)
                .decodeList<SalonDto>()

            salonsResponse.map { salonDto ->
                coroutineScope {
                    val servicesDeferred = async { getServicesForSalon(salonDto.id) }
                    val employeesDeferred = async { getEmployeesForSalon(salonDto.id) }
                    val imagesDeferred = async { getImagesForSalon(salonDto.id) }
                    val hoursDeferred = async { getOpeningHoursForSalon(salonDto.id) }

                    val services = servicesDeferred.await()
                    val employees = employeesDeferred.await()
                    val images = imagesDeferred.await()
                    val hours = hoursDeferred.await()

                    salonDto.toDomain(
                        services = services,
                        employees = employees,
                        images = images,
                        operatingHours = hours
                    )
                }
            }
        } catch (e: Exception) {
            println("Error fetching salons by category: ${e.message}")
            throw e
        }
    }

    // Helper functions to fetch related data
    private suspend fun getServicesForSalon(salonId: String): List<com.techtactoe.ayna.domain.model.Service> {
        return try {
            client
                .from("services")
                .select(Columns.ALL)
                .eq("salon_id", salonId)
                .eq("is_active", true)
                .order("price_cents", ascending = true)
                .decodeList<ServiceDto>()
                .map { it.toDomain() }
        } catch (e: Exception) {
            println("Error fetching services for salon $salonId: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getEmployeesForSalon(salonId: String): List<com.techtactoe.ayna.domain.model.Employee> {
        return try {
            client
                .from("employees")
                .select(Columns.ALL)
                .eq("salon_id", salonId)
                .eq("is_active", true)
                .order("rating", ascending = false)
                .decodeList<EmployeeDto>()
                .map { it.toDomain() }
        } catch (e: Exception) {
            println("Error fetching employees for salon $salonId: ${e.message}")
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

    private suspend fun getOpeningHoursForSalon(salonId: String): Map<DayOfWeek, String> {
        return try {
            val hours = client
                .from("opening_hours")
                .select(Columns.ALL)
                .eq("salon_id", salonId)
                .order("day_of_week", ascending = true)
                .decodeList<OpeningHourDto>()

            hours.associate { hour ->
                val dayOfWeek = when (hour.dayOfWeek) {
                    0 -> DayOfWeek.SUNDAY
                    1 -> DayOfWeek.MONDAY
                    2 -> DayOfWeek.TUESDAY
                    3 -> DayOfWeek.WEDNESDAY
                    4 -> DayOfWeek.THURSDAY
                    5 -> DayOfWeek.FRIDAY
                    6 -> DayOfWeek.SATURDAY
                    else -> DayOfWeek.MONDAY
                }

                val timeString = if (hour.isOpen && hour.openTime != null && hour.closeTime != null) {
                    "${hour.openTime} - ${hour.closeTime}"
                } else {
                    "Closed"
                }

                dayOfWeek to timeString
            }
        } catch (e: Exception) {
            println("Error fetching opening hours for salon $salonId: ${e.message}")
            emptyMap()
        }
    }
}