package com.techtactoe.ayna.domain.model

/**
 * Represents a salon employee/specialist
 */
data class Employee(
    val id: String,
    val name: String,
    val specialty: String,
    val imageUrl: String? = null,
    val rating: Double = 0.0,
    val reviewCount: Int = 0
)
