package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SalonDetail(
    val id: String,
    val name: String,
    val rating: Double,
    val reviewCount: Int,
    val address: String,
    val status: SalonStatus,
    val images: List<String>,
    val services: List<SalonService>,
    val team: List<TeamMember>,
    val reviews: List<Review>,
    val buyOptions: List<BuyOption>,
    val about: SalonAbout,
    val openingHours: List<OpeningHour>
)