package com.techtactoe.ayna.domain.model

/**
 * Represents a service offered by a salon
 */
data class Service(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val durationInMinutes: Int,
    val category: String = "General"
)
