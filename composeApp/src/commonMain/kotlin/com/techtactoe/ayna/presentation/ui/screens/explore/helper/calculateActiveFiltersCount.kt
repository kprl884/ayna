package com.techtactoe.ayna.presentation.ui.screens.explore.helper

/**
 * Helper function to calculate active filters count
 * Following Single Responsibility Principle
 */
fun calculateActiveFiltersCount(filters: com.techtactoe.ayna.domain.model.ExploreFiltersUiModel): Int {
    var count = 0

    if (filters.searchQuery.isNotBlank()) count++
    if (filters.selectedCity.isNotBlank()) count++
    if (filters.sortOption != com.techtactoe.ayna.domain.model.SortOption.RECOMMENDED) count++
    if (filters.priceRange.max < 30000) count++
    if (filters.venueType != com.techtactoe.ayna.domain.model.VenueType.EVERYONE) count++

    return count
}