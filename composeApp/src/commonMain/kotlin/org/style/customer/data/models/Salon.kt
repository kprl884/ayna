package org.style.customer.data.models

data class Salon(
    val id: String,
    val name: String,
    val category: String,
    val rating: Float,
    val priceRange: String,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val isNew: Boolean = false,
    val isTrending: Boolean = false,
    val description: String = "",
    val address: String = "",
    val phone: String = "",
    val workingHours: String = ""
) 