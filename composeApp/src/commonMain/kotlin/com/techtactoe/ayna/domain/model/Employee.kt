package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a salon employee/specialist
 */
@Serializable
data class Employee(
    val id: String,
    val name: String,
    val specialty: String,
    val imageUrl: String? = null,
    val rating: Double = 0.0,
    val reviewCount: Int = 0
)
