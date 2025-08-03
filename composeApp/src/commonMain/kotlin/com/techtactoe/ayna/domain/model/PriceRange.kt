package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PriceRange(
    val min: Int = 0,
    val max: Int = 30000
)