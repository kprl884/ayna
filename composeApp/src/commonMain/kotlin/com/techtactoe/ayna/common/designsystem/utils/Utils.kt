package com.techtactoe.ayna.common.designsystem.utils

import com.techtactoe.ayna.domain.model.SortOption
import com.techtactoe.ayna.domain.model.VenueType

object Utils {
    fun getSortOptionDisplayName(option: SortOption): String {
        return when (option) {
            SortOption.RECOMMENDED -> "Recommended"
            SortOption.TOP_RATED -> "Top-rated"
            SortOption.NEAREST -> "Nearest"
            SortOption.PRICE_LOW_TO_HIGH -> "PRICE_LOW_TO_HIGH"
            SortOption.PRICE_HIGH_TO_LOW -> "PRICE_HIGH_TO_LOW"
        }
    }

    fun getVenueTypeDisplayName(type: VenueType): String {
        return when (type) {
            VenueType.EVERYONE -> "Everyone"
            VenueType.MALE_ONLY -> "Male only"
            VenueType.FEMALE_ONLY -> "Female only"
        }
    }
}