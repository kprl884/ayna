package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SalonAbout(
    val description: String,
    val fullDescription: String
)