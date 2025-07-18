package org.style.customer.model

data class NearbyVenue(
    val id: String,
    val name: String,
    val rating: Float,
    val reviewCount: Int,
    val address: String,
    val type: String
) 