package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class VenueType {
    EVERYONE,
    MALE_ONLY,
    FEMALE_ONLY
}