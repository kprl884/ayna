package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TeamMember(
    val id: String,
    val name: String,
    val role: String,
    val imageUrl: String?,
    val rating: Double
)