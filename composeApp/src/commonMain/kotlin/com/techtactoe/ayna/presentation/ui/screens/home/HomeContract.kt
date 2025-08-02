package com.techtactoe.ayna.presentation.ui.screens.home

import com.techtactoe.ayna.domain.model.Salon

/**
 * Contract defining the UI state and events for the Home screen
 * Following the standardized MVVM pattern
 */
interface HomeContract {

    /**
     * Single source of truth for all UI state in the Home screen
     */
    data class UiState(
        // Data
        val salons: List<Salon> = emptyList(),

        // Loading states
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,

        // Error state
        val errorMessage: String? = null,

        // UI state
        val searchQuery: String = "",
        val userName: String = "User" // In real app, from auth
    )

    /**
     * All possible user interactions with the Home screen
     */
    sealed interface UiEvent {
        // Data events
        data object OnInitialize : UiEvent
        data object OnRefresh : UiEvent

        // Search events
        data class OnSearchQueryChanged(val query: String) : UiEvent
        data object OnSearchClick : UiEvent

        data class OnSalonClick(val salonId: String) : UiEvent
        data object OnProfileClick : UiEvent

        // Error handling
        data object OnClearError : UiEvent
    }
}