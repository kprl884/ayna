package com.techtactoe.ayna.presentation.viewmodel

import com.techtactoe.ayna.domain.model.Salon

data class HomeScreenState(
    val isLoading: Boolean = false,
    val salons: List<Salon> = emptyList(),
    val error: String? = null
) 