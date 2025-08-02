package com.techtactoe.ayna.presentation.ui.screens.salon.model

sealed interface SalonDetailEffect {
    data object NavigateUp : SalonDetailEffect
    data class NavigateToSelectTime(val salonId: String, val serviceId: String) : SalonDetailEffect
    data class Share(val text: String) : SalonDetailEffect
}