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

@Serializable
data class SalonService(
    val id: String,
    val name: String,
    val duration: String,
    val serviceCount: Int,
    val genderRestriction: String? = null,
    val priceFrom: String,
    val category: ServiceCategoryEnum
)

@Serializable
data class TeamMember(
    val id: String,
    val name: String,
    val role: String,
    val imageUrl: String?,
    val rating: Double
)

@Serializable
data class Review(
    val id: String,
    val userName: String,
    val userInitials: String,
    val date: String,
    val rating: Int,
    val comment: String
)

@Serializable
data class BuyOption(
    val id: String,
    val title: String,
    val description: String,
    val type: BuyOptionType
)

@Serializable
data class SalonAbout(
    val description: String,
    val fullDescription: String
)

@Serializable
data class OpeningHour(
    val day: String,
    val isOpen: Boolean,
    val openTime: String? = null,
    val closeTime: String? = null
)

@Serializable
enum class SalonStatus {
    OPEN, CLOSED, OPENS_LATER
}

@Serializable
enum class ServiceCategoryEnum {
    FEATURED,
    CONSULTATION,
    MENS_CUT,
    WOMENS_HAIRCUT,
    STYLE,
    COLOR_APPLICATION,
    QIQI_STRAIGHTENING,
    KIDS
}

@Serializable
enum class BuyOptionType {
    MEMBERSHIP, GIFT_CARD
}
