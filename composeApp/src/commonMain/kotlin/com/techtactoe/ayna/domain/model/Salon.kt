package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Salon(
    val id: String,
    val name: String,
    val location: Location,
    val imageUrls: List<String>,
    val operatingHours: Map<DayOfWeek, String>,
    val employees: List<Employee>,
    val services: List<Service>,
    val description: String = "",
    val phoneNumber: String = "",
    val isOpen: Boolean = true,
    val address: String,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val tags: List<String>
)

