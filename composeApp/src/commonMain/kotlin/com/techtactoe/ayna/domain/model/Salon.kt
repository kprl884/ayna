package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Salon(
    val id: String,
    val name: String,
    val address: String,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val tags: List<String>
) 