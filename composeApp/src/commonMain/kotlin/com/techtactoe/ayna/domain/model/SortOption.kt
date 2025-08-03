package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SortOption {
    RECOMMENDED,
    TOP_RATED,
    NEAREST,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW
}