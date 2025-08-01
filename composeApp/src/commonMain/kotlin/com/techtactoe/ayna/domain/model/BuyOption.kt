package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BuyOption(
    val id: String,
    val title: String,
    val description: String,
    val type: BuyOptionType
)