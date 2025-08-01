package com.techtactoe.ayna.domain.model

/**
 * Enhanced Salon domain model following Clean Architecture patterns
 */
data class SalonV2(
    val id: String,
    val name: String,
    val location: Location,
    val imageUrls: List<String>,
    val rating: Double,
    val reviewCount: Int,
    val operatingHours: Map<DayOfWeek, String>, // e.g., DayOfWeek.MONDAY to "9:00 AM - 7:00 PM"
    val employees: List<Employee>,
    val services: List<Service>,
    val description: String = "",
    val phoneNumber: String = "",
    val isOpen: Boolean = true
)
