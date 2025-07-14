package org.style.customer.model

data class Salon(
    val id: String,
    val name: String,
    val category: String,
    val rating: Float,
    val priceRange: String,
    val imageUrl: String,
    val isFavorite: Boolean,
    val isNew: Boolean,
    val isTrending: Boolean,
    val description: String,
    val address: String,
    val phone: String,
    val workingHours: String,
    val photos: List<String>,
    val reviewCount: Int,
    val location: String,
    val status: String,
    val services: List<Service>,
    val team: List<TeamMember>
)