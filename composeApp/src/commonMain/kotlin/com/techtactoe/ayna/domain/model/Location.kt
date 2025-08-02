package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a physical location with address and coordinates
 */
@Serializable
data class Location(
    val address: String,
    val city: String,
    val latitude: Double,
    val longitude: Double
)
