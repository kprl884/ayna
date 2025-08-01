package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.domain.model.DayOfWeek
import com.techtactoe.ayna.domain.model.Employee
import com.techtactoe.ayna.domain.model.Location
import com.techtactoe.ayna.domain.model.SalonV2
import com.techtactoe.ayna.domain.model.Service
import com.techtactoe.ayna.domain.repository.SalonRepositoryV2
import kotlinx.coroutines.delay
import kotlin.math.exp
import kotlin.math.sqrt

/**
 * Mock implementation of SalonRepositoryV2 with realistic hardcoded data
 */
class MockSalonRepositoryV2Impl : SalonRepositoryV2 {

    private val mockSalons = listOf(
        SalonV2(
            id = "1",
            name = "Emre's Barbershop Dolapdere",
            location = Location(
                address = "Dolapdere Mahallesi, 34384 Istanbul",
                city = "Istanbul",
                latitude = 41.0082,
                longitude = 28.9784
            ),
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=800",
                "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=800",
                "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800"
            ),
            rating = 5.0,
            reviewCount = 69,
            operatingHours = mapOf(
                DayOfWeek.MONDAY to "9:00 AM - 7:00 PM",
                DayOfWeek.TUESDAY to "9:00 AM - 7:00 PM",
                DayOfWeek.WEDNESDAY to "9:00 AM - 7:00 PM",
                DayOfWeek.THURSDAY to "9:00 AM - 7:00 PM",
                DayOfWeek.FRIDAY to "9:00 AM - 8:00 PM",
                DayOfWeek.SATURDAY to "8:00 AM - 8:00 PM",
                DayOfWeek.SUNDAY to "Closed"
            ),
            employees = listOf(
                Employee(
                    "emp1",
                    "Emre Demir",
                    "Master Barber",
                    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
                    4.9,
                    45
                ),
                Employee(
                    "emp2",
                    "Ali Yılmaz",
                    "Hair Stylist",
                    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400",
                    4.8,
                    32
                )
            ),
            services = listOf(
                Service(
                    "svc1",
                    "Haircut / Saç Kesimi",
                    "Professional men's haircut with styling",
                    700.0,
                    60
                ),
                Service(
                    "svc2",
                    "Haircut & Shave / Saç Kesimi & Sakal Tıraşı",
                    "Complete grooming package",
                    1175.0,
                    90
                ),
                Service(
                    "svc3",
                    "Full Service / Komple Bakım",
                    "Premium grooming experience",
                    1880.0,
                    120
                )
            ),
            description = "Traditional Turkish barbershop offering premium men's grooming services in the heart of Dolapdere.",
            phoneNumber = "+90 212 555 0101"
        ),
        SalonV2(
            id = "2",
            name = "Ayna Beauty Studio",
            location = Location(
                address = "Fenerbahçe Mahallesi, Rüştiye Sokak 9, Istanbul",
                city = "Istanbul",
                latitude = 40.9789,
                longitude = 29.0372
            ),
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1580618672591-eb180b1a973f?w=800",
                "https://images.unsplash.com/photo-1522336284037-91f7da073525?w=800"
            ),
            rating = 5.0,
            reviewCount = 158,
            operatingHours = mapOf(
                DayOfWeek.MONDAY to "10:00 AM - 8:00 PM",
                DayOfWeek.TUESDAY to "10:00 AM - 8:00 PM",
                DayOfWeek.WEDNESDAY to "10:00 AM - 8:00 PM",
                DayOfWeek.THURSDAY to "10:00 AM - 8:00 PM",
                DayOfWeek.FRIDAY to "10:00 AM - 9:00 PM",
                DayOfWeek.SATURDAY to "9:00 AM - 9:00 PM",
                DayOfWeek.SUNDAY to "11:00 AM - 7:00 PM"
            ),
            employees = listOf(
                Employee(
                    "emp3",
                    "Ayla Kaya",
                    "Senior Nail Artist",
                    "https://images.unsplash.com/photo-1494790108755-2616b612b3-2?w=400",
                    4.9,
                    78
                ),
                Employee(
                    "emp4",
                    "Zeynep Özkan",
                    "Beauty Specialist",
                    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
                    4.8,
                    56
                )
            ),
            services = listOf(
                Service(
                    "svc4",
                    "Spa Pedikür (Spa Pedicure)",
                    "Relaxing spa pedicure treatment",
                    950.0,
                    55
                ),
                Service(
                    "svc5",
                    "SMART Manikür + Kalıcı Oje",
                    "Smart manicure with gel polish",
                    1200.0,
                    100
                ),
                Service("svc6", "Nail Art", "Creative nail art designs", 0.0, 20)
            ),
            description = "Premium beauty studio specializing in nail care and beauty treatments.",
            phoneNumber = "+90 216 555 0202"
        ),
        SalonV2(
            id = "3",
            name = "Serenity Spa & Wellness",
            location = Location(
                address = "Bebek Mahallesi, 34342 Istanbul",
                city = "Istanbul",
                latitude = 41.0766,
                longitude = 29.0434
            ),
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=800",
                "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800"
            ),
            rating = 4.8,
            reviewCount = 234,
            operatingHours = mapOf(
                DayOfWeek.MONDAY to "8:00 AM - 10:00 PM",
                DayOfWeek.TUESDAY to "8:00 AM - 10:00 PM",
                DayOfWeek.WEDNESDAY to "8:00 AM - 10:00 PM",
                DayOfWeek.THURSDAY to "8:00 AM - 10:00 PM",
                DayOfWeek.FRIDAY to "8:00 AM - 10:00 PM",
                DayOfWeek.SATURDAY to "8:00 AM - 10:00 PM",
                DayOfWeek.SUNDAY to "9:00 AM - 9:00 PM"
            ),
            employees = listOf(
                Employee(
                    "emp5",
                    "Mehmet Güler",
                    "Massage Therapist",
                    "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400",
                    4.9,
                    89
                ),
                Employee(
                    "emp6",
                    "Fatma Yıldız",
                    "Wellness Specialist",
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?w=400",
                    4.7,
                    67
                )
            ),
            services = listOf(
                Service(
                    "svc7",
                    "60 Min Thai with Oil Massage",
                    "Traditional Thai massage with aromatic oils",
                    1600.0,
                    75
                ),
                Service("svc8", "30 Min Foot Massage", "Relaxing foot massage therapy", 800.0, 30),
                Service("svc9", "60 Min Oil Massage", "Full body oil massage", 1600.0, 75)
            ),
            description = "Luxury spa offering traditional and modern wellness treatments in Bebek.",
            phoneNumber = "+90 212 555 0303"
        ),
        SalonV2(
            id = "4",
            name = "Elite Hair Studio",
            location = Location(
                address = "Nişantaşı Mahallesi, 34365 Istanbul",
                city = "Istanbul",
                latitude = 41.0460,
                longitude = 28.9940
            ),
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1562322140-8baeececf3df?w=800"
            ),
            rating = 4.7,
            reviewCount = 89,
            operatingHours = mapOf(
                DayOfWeek.MONDAY to "9:00 AM - 8:00 PM",
                DayOfWeek.TUESDAY to "9:00 AM - 8:00 PM",
                DayOfWeek.WEDNESDAY to "9:00 AM - 8:00 PM",
                DayOfWeek.THURSDAY to "9:00 AM - 8:00 PM",
                DayOfWeek.FRIDAY to "9:00 AM - 8:00 PM",
                DayOfWeek.SATURDAY to "9:00 AM - 8:00 PM",
                DayOfWeek.SUNDAY to "Closed"
            ),
            employees = listOf(
                Employee(
                    "emp7",
                    "Canan Arslan",
                    "Senior Hair Stylist",
                    "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400",
                    4.8,
                    92
                ),
                Employee(
                    "emp8",
                    "Deniz Koç",
                    "Color Specialist",
                    "https://images.unsplash.com/photo-1494790108755-2616b612b3da?w=400",
                    4.6,
                    73
                )
            ),
            services = listOf(
                Service(
                    "svc10",
                    "Women's Cut & Style",
                    "Professional women's haircut and styling",
                    2000.0,
                    90
                ),
                Service("svc11", "Color Treatment", "Hair coloring and highlights", 3500.0, 180),
                Service("svc12", "Blowout", "Professional hair blowout", 1000.0, 45)
            ),
            description = "High-end hair studio in Nişantaşı offering premium hair services for women.",
            phoneNumber = "+90 212 555 0404"
        )
    )

    override suspend fun getRecommendedSalons(): List<SalonV2> {
        delay(1500) // Simulate network latency
        return mockSalons
    }

    override suspend fun searchSalons(query: String): List<SalonV2> {
        delay(1000) // Simulate network latency
        return mockSalons.filter { salon ->
            salon.name.contains(query, ignoreCase = true) ||
                    salon.services.any { it.name.contains(query, ignoreCase = true) } ||
                    salon.employees.any { it.name.contains(query, ignoreCase = true) }
        }
    }

    override suspend fun getSalonDetails(id: String): SalonV2? {
        delay(800) // Simulate network latency
        return mockSalons.find { it.id == id }
    }

    override suspend fun getSalonsNearLocation(
        latitude: Double,
        longitude: Double,
        radiusKm: Int
    ): List<SalonV2> {
        delay(1200) // Simulate network latency
        // Simple distance calculation (in real app would use proper geolocation)
        return mockSalons.filter { salon ->
            val distance = sqrt(
                exp(salon.location.latitude - latitude) +
                        exp(salon.location.longitude - longitude)
            ) * 111 // Rough conversion to km
            distance <= radiusKm
        }
    }

    override suspend fun getSalonsByCategory(category: String): List<SalonV2> {
        delay(900) // Simulate network latency
        return mockSalons.filter { salon ->
            salon.services.any { it.category.equals(category, ignoreCase = true) }
        }
    }
}
