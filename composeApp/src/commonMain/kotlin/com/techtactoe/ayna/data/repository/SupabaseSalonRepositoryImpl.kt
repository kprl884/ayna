package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import com.techtactoe.ayna.data.supabase.dto.EmployeeDto
import com.techtactoe.ayna.data.supabase.dto.OpeningHourDto
import com.techtactoe.ayna.data.supabase.dto.SalonDto
import com.techtactoe.ayna.data.supabase.dto.SalonImageDto
import com.techtactoe.ayna.data.supabase.dto.ServiceDto
import com.techtactoe.ayna.data.supabase.mapper.*
import com.techtactoe.ayna.domain.model.DayOfWeek
import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.repository.SalonRepository
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import com.techtactoe.ayna.data.supabase.mapper.salonDtoToSalonDomainModel as toDomain

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

            // Fetch related data for each salon concurrently
            coroutineScope {
                val deferredSalons = salonsResponse.map { salonDto: SalonDto ->
                    async {
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
                deferredSalons.awaitAll()
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
                .select(columns = Columns.ALL) {
                    filter { eq("is_featured", true) }
                    order("rating", order = Order.DESCENDING)
                }
                .decodeList<SalonDto>()

            // Fetch related data for each salon concurrently
            coroutineScope {
                val deferredSalons = salonsResponse.map { salonDto: SalonDto ->
                    async {
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
                deferredSalons.awaitAll()
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
                .select(columns = Columns.ALL) {
                    filter { textSearch("name", query, TextSearchType.WEBSEARCH) }
                }
                .decodeList<SalonDto>()

            // Also search in services
            val serviceResults = client
                .from("services")
                .select(columns = Columns.raw("salon_id")) {
                    filter { textSearch("name", query, TextSearchType.WEBSEARCH) }
                }
                .decodeList<Map<String, String>>()

            val salonIdsFromServices =
                serviceResults.mapNotNull { map: Map<String, String> -> map["salon_id"] }

            // Combine results
            val allSalonIds =
                (salonsResponse.map { dto: SalonDto -> dto.id } + salonIdsFromServices).distinct()

            val allSalons = if (allSalonIds.isNotEmpty()) {
                client
                    .from("salons")
                    .select(columns = Columns.ALL)
                    .decodeList<SalonDto>()
                    .filter { dto -> dto.id in allSalonIds }
            } else {
                salonsResponse
            }

            coroutineScope {
                val deferredSalons = allSalons.map { salonDto: SalonDto ->
                    async {
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
                deferredSalons.awaitAll()
            }
        } catch (e: Exception) {
            println("Error searching salons: ${e.message}")
            throw e
        }
    }

    override suspend fun getSalonDetails(id: String): Salon? {
        return try {
            val salonResponse: SalonDto? = client
                .from("salons")
                .select(columns = Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeSingleOrNull<SalonDto>()

            if (salonResponse != null) {
                val salonDto: SalonDto = salonResponse
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
            } else {
                null
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
            val salonsResponse = client.rpc(
                "salons_within_radius",
                mapOf(
                    "lat" to latitude,
                    "lng" to longitude,
                    "radius_km" to radiusKm
                )
            )
                .decodeList<SalonDto>()

            coroutineScope {
                val deferredSalons = salonsResponse.map { salonDto: SalonDto ->
                    async {
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
                deferredSalons.awaitAll()
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
                .select(columns = Columns.raw("salon_id")) {
                    filter { eq("category_id", category) }
                }
                .decodeList<Map<String, String>>()

            val salonIds = serviceResults.mapNotNull { map: Map<String, String> -> map["salon_id"] }

            if (salonIds.isEmpty()) {
                return emptyList()
            }

            val salonsResponse = client
                .from("salons")
                .select(columns = Columns.ALL)
                .decodeList<SalonDto>()
                .filter { dto -> dto.id in salonIds }

            coroutineScope {
                val deferredSalons = salonsResponse.map { salonDto: SalonDto ->
                    async {
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
                deferredSalons.awaitAll()
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
                .select(columns = Columns.ALL) {
                    filter {
                        eq("salon_id", salonId)
                        eq("is_active", true)
                    }
                    order("price_cents", order = Order.ASCENDING)
                }
                .decodeList<ServiceDto>()
                .map { dto: ServiceDto -> dto.serviceDtoToServiceDomainModel() }
        } catch (e: Exception) {
            println("Error fetching services for salon $salonId: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getEmployeesForSalon(salonId: String): List<com.techtactoe.ayna.domain.model.Employee> {
        return try {
            client
                .from("employees")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("salon_id", salonId)
                        eq("is_active", true)
                    }
                    order("rating", order = Order.DESCENDING)
                }
                .decodeList<EmployeeDto>()
                .map { dto: EmployeeDto -> dto.employeeDtoToEmployeeDomainModel() }
        } catch (e: Exception) {
            println("Error fetching employees for salon $salonId: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getImagesForSalon(salonId: String): List<String> {
        return try {
            client
                .from("salon_images")
                .select(columns = Columns.raw("image_url")) {
                    filter { eq("salon_id", salonId) }
                    order("sort_order", order = Order.ASCENDING)
                }
                .decodeList<SalonImageDto>()
                .map { dto: SalonImageDto -> dto.imageUrl }
        } catch (e: Exception) {
            println("Error fetching images for salon $salonId: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getOpeningHoursForSalon(salonId: String): Map<DayOfWeek, String> {
        return try {
            val hours = client
                .from("opening_hours")
                .select(columns = Columns.ALL) {
                    filter { eq("salon_id", salonId) }
                    order("day_of_week", order = Order.ASCENDING)
                }
                .decodeList<OpeningHourDto>()

            hours.associate { hour: OpeningHourDto ->
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

                val timeString =
                    if (hour.isOpen && hour.openTime != null && hour.closeTime != null) {
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