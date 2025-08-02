package com.techtactoe.ayna.presentation.ui.screens.salon

import com.techtactoe.ayna.domain.model.SalonDetail
import com.techtactoe.ayna.presentation.ui.components.SalonDetailTab

/**
 * Contract defining the UI state and events for the SalonDetail screen
 * Following the standardized MVVM pattern
 */
interface SalonDetailContract {
    
    /**
     * Single source of truth for all UI state in the SalonDetail screen
     */
    data class UiState(
        // Data
        val salonDetail: SalonDetail? = null,
        val salonId: String = "",
        
        // UI state
        val selectedTab: SalonDetailTab = SalonDetailTab.SERVICES,
        val showStickyTabBar: Boolean = false,
        val isFavorite: Boolean = false,
        
        // Loading states
        val isLoading: Boolean = true,
        val isBookingInProgress: Boolean = false,
        
        // Error state
        val errorMessage: String? = null,
        
        // Navigation flags
        val navigateBack: Boolean = false,
        val navigateToBooking: Boolean = false,
        val navigateToServiceBooking: String? = null,
        val shareContent: String? = null
    )
    
    /**
     * All possible user interactions with the SalonDetail screen
     */
    sealed interface UiEvent {
        // Initialization
        data class OnInitialize(val salonId: String) : UiEvent
        
        // Tab events
        data class OnTabSelected(val tab: SalonDetailTab) : UiEvent
        data class OnScrollStateChanged(val showStickyTabBar: Boolean) : UiEvent
        
        // Action events
        data object OnBackClick : UiEvent
        data object OnShareClick : UiEvent
        data object OnFavoriteClick : UiEvent
        data object OnBookNowClick : UiEvent
        data class OnServiceBookClick(val serviceId: String) : UiEvent
        
        // Navigation events
        data class OnNavigationHandled(val resetNavigation: NavigationReset) : UiEvent
        
        // Error handling
        data object OnClearError : UiEvent
    }
    
    /**
     * Navigation reset options
     */
    enum class NavigationReset {
        BACK,
        BOOKING,
        SERVICE_BOOKING,
        SHARE
    }
}
