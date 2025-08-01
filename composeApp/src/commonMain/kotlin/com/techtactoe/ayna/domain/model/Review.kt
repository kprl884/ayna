package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: String,
    val userName: String,
    val userInitials: String,
    val date: String,
    val rating: Int,
    val comment: String
)