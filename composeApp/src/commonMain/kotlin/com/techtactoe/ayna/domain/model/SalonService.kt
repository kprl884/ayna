package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

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