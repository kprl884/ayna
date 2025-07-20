package com.techtactoe.ayna.domain.repository

import com.techtactoe.ayna.domain.model.Salon

interface SalonRepository {
    suspend fun getNearbySalons(): List<Salon>
} 