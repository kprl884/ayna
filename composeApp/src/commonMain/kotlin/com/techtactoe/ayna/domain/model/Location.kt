package com.techtactoe.ayna.domain.model

/**
 * Represents a physical location with address and coordinates
 */
data class Location(
    val address: String,
    val city: String,
    val latitude: Double,
    val longitude: Double
)
